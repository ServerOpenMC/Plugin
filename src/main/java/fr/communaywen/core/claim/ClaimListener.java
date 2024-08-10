package fr.communaywen.core.claim;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.EconomieTeam;
import fr.communaywen.core.teams.Team;

public class ClaimListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!GamePlayer.gamePlayers.containsKey(event.getPlayer().getName())) new GamePlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        checkRegion(player, event.getBlock(), event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ARMOR_STAND) {
            Player player = (Player) event.getDamager();
            checkRegion(player, event.getEntity().getLocation().getBlock(), event);
        }
    }

    @EventHandler
    public void onPistonFlip(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                if (region.isInArea(block.getLocation()) || region.isInArea(block.getRelative(event.getDirection()).getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                if (region.isInArea(block.getLocation()) || region.isInArea(block.getRelative(event.getDirection()).getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        checkRegion(player, event.getRightClicked().getLocation().getBlock(), event);
    
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            ItemStack item = player.getItemInHand();
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                player.sendMessage("§cVous ne pouvez pas utiliser un BATON DE CLAIM sur un cadre d'item.");
            }
        }
    }
    
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory() != null && event.getWhoClicked() instanceof Player) {
            ItemStack item = event.getCursor();
            if(item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                switch (event.getClickedInventory().getType()) {
                    case CHEST, ENDER_CHEST, DISPENSER, DROPPER, HOPPER, BARREL, SHULKER_BOX, WORKBENCH, FURNACE, LOOM, GRINDSTONE, SMOKER, BLAST_FURNACE, CARTOGRAPHY, STONECUTTER, BEACON, ENCHANTING, ANVIL, BREWING, MERCHANT, CRAFTING:
                        event.setCancelled(true);
                        ((Player) event.getWhoClicked()).sendMessage("§cVous ne pouvez pas utiliser un BATON DE CLAIM dans cet inventaire.");
                        break;
                    default:
                        break;
                }
            }
        }

        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
            if (hotbarItem != null && hotbarItem.getType() == Material.STICK && Objects.requireNonNull(hotbarItem.getItemMeta()).hasDisplayName() && hotbarItem.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                player.sendMessage("§cVous ne pouvez pas placer un BATON DE CLAIM dans cet inventaire.");
            }
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null && currentItem.getType() == Material.STICK && Objects.requireNonNull(currentItem.getItemMeta()).hasDisplayName() && currentItem.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                player.sendMessage("§cVous ne pouvez pas déplacer un BATON DE CLAIM dans cet inventaire.");
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cVous ne pouvez pas jeter un BATON DE CLAIM.");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(event.getBlock() == null) return;
        checkRegion(player, event.getBlock(), event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        handleExplosion(event.blockList());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        handleExplosion(event.blockList());
    }

    private void handleExplosion(List<Block> blocks) {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                if (region.isInArea(block.getLocation())) {
                    iterator.remove();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == Material.BONE_MEAL) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.MOSS_BLOCK) {
                Player player = event.getPlayer();

                int radius = 6;
                boolean isInAnyRegion = false;
                outerLoop:
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block nearbyBlock = clickedBlock.getRelative(x, y, z);
                            for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                                if (region.isInArea(nearbyBlock.getLocation()) && !region.isTeamMember(player.getUniqueId())) {
                                    isInAnyRegion = true;
                                    break outerLoop;
                                }
                            }
                        }
                    }
                }

                if (isInAnyRegion) {
                    event.setCancelled(true);
                    player.sendMessage("§cLa propagation de la mousse est désactivée car il y a un claim à proximité.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        if (event.getBucket().equals(Material.WATER_BUCKET) || event.getBucket().equals(Material.LAVA_BUCKET) || event.getBucket().equals(Material.POWDER_SNOW_BUCKET) || event.getBucket().equals(Material.BUCKET)) {
            checkRegion(player, block, event);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player) {
                    ((Player) event.getWhoClicked()).sendMessage("§cVous ne pouvez pas utiliser un BATON DE CLAIM pour crafter.");
                }
                break;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock().getRelative(event.getBlockFace());

            checkRegion(player, block, event);
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null) {
            ItemStack item = player.getItemInHand();
            if (player.getItemInHand().getType() == Material.STICK && item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);

                GamePlayer gp = GamePlayer.gamePlayers.get(player.getName());
                Team playerTeam = AywenCraftPlugin.getInstance().getManagers().getTeamManager().getTeamByPlayer(playerUuid);

                if (gp.getPos1() == null) {

                    // Check if player is in world
                    if (!player.getWorld().getName().equals("world")) {
                        removeClaimStick(player);
                        player.sendMessage("§cVous ne pouvez pas créer de région dans ce monde !");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    if (playerTeam == null) {
                        removeClaimStick(player);
                        player.sendMessage("§cVous devez être dans une équipe pour créer une région !");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    if(ClaimConfigDataBase.getCountClaims(playerTeam.getName()) >= 5) {
                        removeClaimStick(player);
                        player.sendMessage("§cVotre team possède déjà 5 claims, qui est la limite maximale de claim possible par team.");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    gp.setPos1(event.getClickedBlock().getLocation());
                    player.sendMessage("§aPosition 1 défini.");

                    Bukkit.getScheduler().runTaskLater(AywenCraftPlugin.getInstance(), () -> {
                        removeClaimStick(player);
                        gp.setPos1(null);
                        gp.setPos2(null);
                    }, 20 * 60 * 5); // 5 Minutes sans interactions
                    return;
                }

                if (gp.getPos1() != null && gp.getPos2() == null) {
                    player.getInventory().removeItem(player.getItemInHand());
                    gp.setPos2(event.getClickedBlock().getLocation());

                    if (!Objects.equals(gp.getPos1().getWorld(), gp.getPos2().getWorld())) {
                        removeClaimStick(player);
                        player.sendMessage("§cVous devez rester dans le même monde entre les deux points !");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    } else if(GamePlayer.isRegionConflict(player, gp.getPos1(), gp.getPos2())) {
                        removeClaimStick(player);
                        player.sendMessage("§cUne régions WorldGuard traverse votre claim.");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    double distance = gp.getPos1().distance(gp.getPos2());
                    double balance = EconomieTeam.getTeamBalances(playerTeam.getName());
                    double cost = (50 + (int) (distance * 2));

                    if(balance < cost) {
                        player.sendMessage("§cVotre équipe n'a pas assez d'argent pour créer ce claim. Coût: " + cost + "$.");
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    RegionManager region = new RegionManager(gp.getPos1(), gp.getPos2(), playerTeam);

                    for (RegionManager regionCheck : AywenCraftPlugin.getInstance().regions) {
                        if (regionCheck.isInArea(region.maxLoc) || regionCheck.isInArea(region.minLoc) || regionCheck.isInArea(region.getMiddle())) {
                            player.sendMessage("§cIl y a déjà une région dans cette zone !");
                            gp.setPos1(null);
                            gp.setPos2(null);
                            return;
                        }
                    }

                    EconomieTeam.removeBalance(playerTeam.getName(), cost);
                    ClaimConfigDataBase.addClaims(region.getClaimID(), playerTeam.getName(), gp.getPos1().getX(), gp.getPos1().getZ(), gp.getPos2().getX(), gp.getPos2().getZ(), player.getWorld().getName());
                    AywenCraftPlugin.getInstance().regions.add(region);
        
                    player.sendMessage("§aPosition 2 définie.");
                    player.sendMessage("§aVous venez de créer une nouvelle région.");

                    gp.setPos1(null);
                    gp.setPos2(null);
                }
            }
        }
    }

    public static boolean getClaimStick(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                return true;
            }
        }
        return false;
    }

    private void removeClaimStick(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                player.getInventory().remove(item);
            }
        }
    }

    private void checkRegion(Player player, Block block, Event eventHandler) {
        AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
        for (RegionManager region : plugin.regions) {
            if (region.isInArea(block.getLocation())) {
                boolean isBypassing = AywenCraftPlugin.playerClaimsByPass.contains(player);
                boolean isTeamMember = region.isTeamMember(player.getUniqueId());

                if (!isBypassing && !isTeamMember) {
                    if(player != null) player.sendMessage("§cCe n'est pas chez vous");
                    if (eventHandler instanceof Cancellable cancellable) {
                        cancellable.setCancelled(true);
                        AywenCraftPlugin.getInstance().getLogger().info("Cancelled event: " + eventHandler.getClass().getSimpleName());
                    }
                    return;
                }
            }
        }
    }
}