package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.corpse.CorpseMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CorpseListener implements Listener {

    private final CorpseManager corpseManager;
    private final Map<UUID, List<Item>> waterDeaths = new HashMap<>();

    public CorpseListener(CorpseManager corpseManager) {
        this.corpseManager = corpseManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (player.getWorld().getName().equals("dreamworld")) { return; }

        Location deathLocation = player.getLocation();
        Block blockBelow = deathLocation.getBlock();

        Material typeBelow = blockBelow.getType();
        if (isHalfBlock(typeBelow)) {
            deathLocation.add(0, 1, 0);
        }

        boolean waterNearby = false;
        for (int x = -7; x <= 7; x++) {
            for (int y = -7; y <= 7; y++) {
                for (int z = -7; z <= 7; z++) {
                    Block block = player.getLocation().add(x, y, z).getBlock();
                    if (block.getType() == Material.WATER) {
                        waterNearby = true;
                        break;
                    }
                }
                if (waterNearby) break;
            }
            if (waterNearby) break;
        }

        e.getDrops().clear();

        boolean isArea = false;

        for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
            if(region.isInArea(deathLocation)){
                isArea = true;
                break;
            }
        }

        if (waterNearby || isArea) {
            List<Item> items = new ArrayList<>();
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null) {
                    Item item = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    items.add(item);
                }
            }
            waterDeaths.put(player.getUniqueId(), items);
        } else {
            corpseManager.addCorpse(e.getEntity(), e.getEntity().getInventory(), deathLocation);
        }
    }

    private boolean isHalfBlock(Material type) {
        return switch (type) {
            case DIRT_PATH, OAK_SLAB, SPRUCE_SLAB, BIRCH_SLAB, JUNGLE_SLAB, ACACIA_SLAB, DARK_OAK_SLAB, STONE_SLAB,
                 SANDSTONE_SLAB, COBBLESTONE_SLAB, BRICK_SLAB, STONE_BRICK_SLAB, NETHER_BRICK_SLAB, QUARTZ_SLAB,
                 RED_SANDSTONE_SLAB, PURPUR_SLAB, PRISMARINE_SLAB, PRISMARINE_BRICK_SLAB, DARK_PRISMARINE_SLAB -> true;
            default -> false;
        };
    }


    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            corpseManager.open(e.getClickedBlock().getLocation(), e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (corpseManager.isCorpseInventory(e.getClickedInventory())) {
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                e.setCancelled(true);
            } /*else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER) {
                Player p = (Player) e.getWhoClicked();

                Inventory corpseInventory = e.getClickedInventory();
                Inventory playerInventory = p.getInventory();

                CorpseMenu corpseMenu = corpseManager.getCorpseMenuByPlayer(p);

                if(corpseMenu == null){
                    return;
                }

                corpseMenu.swapContents(playerInventory, corpseInventory);

                p.sendMessage("§aVous avez récupéré tout le stuff de la tombe.");
                p.closeInventory();
            }*/
        }
    }

    @EventHandler
    public void onBreak(CustomBlockBreakEvent e) {
        ItemStack block = e.getCustomBlockItem();

        if (block.hasItemMeta() && block.getItemMeta().hasDisplayName() && block.getItemMeta().getDisplayName().equalsIgnoreCase("§fgrave")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (corpseManager.isCorpseInventory(e.getInventory())) {
            corpseManager.close(e.getInventory());
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Item item = e.getItem();

        for (UUID uuid : waterDeaths.keySet()) {
            List<Item> items = waterDeaths.get(uuid);
            if (items.contains(item)) {
                if (!player.getUniqueId().equals(uuid)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerBucketEmptyEvent e) {
        Player player = e.getPlayer();

        Location blockLocation = e.getBlock().getLocation();
        List<Location> graveLocations = corpseManager.getGraveLocations();

        for (Location graveLocation : graveLocations) {
            if (blockLocation.getWorld().equals(graveLocation.getWorld())) {
                double distance = blockLocation.distance(graveLocation);
                if (distance <= 12 ||
                        (blockLocation.getBlockX() == graveLocation.getBlockX() &&
                                blockLocation.getBlockZ() == graveLocation.getBlockZ() &&
                                blockLocation.getBlockY() >= graveLocation.getBlockY())) {
                    e.setCancelled(true);
                    player.sendMessage("§cVous ne pouvez pas placer de l'eau près d'une tombe.");
                    return;
                }
            }
        }
    }

}
