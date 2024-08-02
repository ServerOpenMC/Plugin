package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.corpse.CorpseMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
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

        boolean waterNearby = false;
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
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
        if (waterNearby) {
            List<Item> items = new ArrayList<>();
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null) {
                    Item item = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    items.add(item);
                }
            }
            waterDeaths.put(player.getUniqueId(), items);
        } else {
            corpseManager.addCorpse(e.getEntity(), e.getEntity().getInventory());
        }
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

}
