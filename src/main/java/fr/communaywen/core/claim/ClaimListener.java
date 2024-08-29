package fr.communaywen.core.claim;

import java.sql.SQLException;
import java.util.*;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
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

    private Map<Block, UUID> recentPistonActivations = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!GamePlayer.gamePlayers.containsKey(event.getPlayer().getName())) new GamePlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkRegion(event.getPlayer(), event.getBlock(), event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        checkRegion(player, event.getEntity().getLocation().getBlock(), event);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        checkRegion(player, event.getRightClicked().getLocation().getBlock(), event);
    
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            ItemStack item = player.getItemInHand();
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                MessageManager.sendMessageType(player, "§cVous ne pouvez pas utiliser un BATON DE CLAIM sur un cadre d'item.", Prefix.CLAIM, MessageType.ERROR, true);
            }
        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory() != null && event.getWhoClicked() instanceof Player) {
            ItemStack item = event.getCursor();
            if(item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                switch (event.getClickedInventory().getType()) {
                    case CHEST, ENDER_CHEST, DISPENSER, DROPPER, HOPPER, BARREL, SHULKER_BOX, WORKBENCH, FURNACE, LOOM, GRINDSTONE, SMOKER, BLAST_FURNACE, CARTOGRAPHY, STONECUTTER, BEACON, ENCHANTING, ANVIL, BREWING, MERCHANT, CRAFTING:
                        event.setCancelled(true);
                        MessageManager.sendMessageType(player, "§cVous ne pouvez pas utiliser un BATON DE CLAIM dans cet inventaire.", Prefix.CLAIM, MessageType.ERROR, true);
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
                MessageManager.sendMessageType(player, "§cVous ne pouvez pas placer un BATON DE CLAIM.", Prefix.CLAIM, MessageType.ERROR, true);
            }
        }

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null && currentItem.getType() == Material.STICK && Objects.requireNonNull(currentItem.getItemMeta()).hasDisplayName() && currentItem.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);
                MessageManager.sendMessageType(player, "§cVous ne pouvez pas déplacer un BATON DE CLAIM.", Prefix.CLAIM, MessageType.ERROR, true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
            event.setCancelled(true);
            MessageManager.sendMessageType(event.getPlayer(), "§cVous ne pouvez pas jeter un BATON DE CLAIM.", Prefix.CLAIM, MessageType.ERROR, true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        assert event.getBlockPlaced() != null;
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
                    MessageManager.sendMessageType((Player) event.getWhoClicked(), "§cVous ne pouvez pas utiliser un BATON DE CLAIM pour crafter.", Prefix.CLAIM, MessageType.ERROR, true);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        assert event.getClickedBlock() != null;
        assert event.getItem() != null;
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block block = event.getClickedBlock().getRelative(event.getBlockFace());
            if (event.getItem() != null && event.getItem().getType() == Material.BONE_MEAL) {
                if (clickedBlock != null && clickedBlock.getType() == Material.MOSS_BLOCK || Objects.requireNonNull(clickedBlock).getType() == Material.AZALEA ||
                        clickedBlock.getType() == Material.FLOWERING_AZALEA || clickedBlock.getType() == Material.MOSS_CARPET ||
                        clickedBlock.getType() == Material.GRASS_BLOCK || clickedBlock.getType() == Material.TALL_GRASS) {
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
                        MessageManager.sendMessageType(player, "§cLa propagation de la mousse est désactivée car il y a un claim à proximité.", Prefix.CLAIM, MessageType.ERROR, true);
                    }
                }
            }

            checkRegion(player, block, event);
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null) {
            ItemStack item = player.getItemInHand();
            if (player.getItemInHand().getType() == Material.STICK && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);

                GamePlayer gp = GamePlayer.gamePlayers.get(player.getName());
                Team playerTeam = AywenCraftPlugin.getInstance().getManagers().getTeamManager().getTeamByPlayer(playerUuid);

                if (gp.getPos1() == null) {

                    // Check if player is in world
                    if (!player.getWorld().getName().equals("world")) {
                        removeClaimStick(player);
                        MessageManager.sendMessageType(player, "§cVous ne pouvez pas créer une région dans ce monde.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    if (playerTeam == null) {
                        removeClaimStick(player);
                        MessageManager.sendMessageType(player, "§cVous n'êtes pas dans une team.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    if(ClaimConfigDataBase.getCountClaims(playerTeam.getName()) >= 5) {
                        removeClaimStick(player);
                        MessageManager.sendMessageType(player, "§cVotre team a atteint la limite de 5 régions.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    gp.setPos1(event.getClickedBlock().getLocation());
                    MessageManager.sendMessageType(player, "§aVous avez défini le point 1 de votre région.", Prefix.CLAIM, MessageType.SUCCESS, true);

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
                        MessageManager.sendMessageType(player, "§cVous ne pouvez pas créer une région dans un autre monde.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    } else if(GamePlayer.isRegionConflict(player, gp.getPos1(), gp.getPos2())) {
                        removeClaimStick(player);
                        MessageManager.sendMessageType(player, "§cVotre région WorldGuard chevauche votre claim.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    double distance = gp.getPos1().distance(gp.getPos2());
                    double balance = EconomieTeam.getTeamBalances(playerTeam.getName());
                    double cost = (50 + (int) (distance * 2));

                    if(balance < cost) {
                        MessageManager.sendMessageType(player, "§cVotre team n'a pas assez d'argent pour créer une région.", Prefix.CLAIM, MessageType.ERROR, true);
                        gp.setPos1(null);
                        gp.setPos2(null);
                        return;
                    }

                    RegionManager region = new RegionManager(gp.getPos1(), gp.getPos2(), playerTeam);

                    for (RegionManager regionCheck : AywenCraftPlugin.getInstance().regions) {
                        if (regionCheck.isInArea(region.maxLoc) || regionCheck.isInArea(region.minLoc) || regionCheck.isInArea(region.getMiddle()) ||
                                region.isInArea(regionCheck.maxLoc) || region.isInArea(regionCheck.minLoc) || region.isInArea(regionCheck.getMiddle())) {
                            MessageManager.sendMessageType(player, "§cVotre région chevauche une autre région.", Prefix.CLAIM, MessageType.ERROR, true);
                            gp.setPos1(null);
                            gp.setPos2(null);
                            return;
                        }
                    }

                    EconomieTeam.removeBalance(playerTeam.getName(), cost);
                    ClaimConfigDataBase.addClaims(region.getClaimID(), playerTeam.getName(), gp.getPos1().getX(), gp.getPos1().getZ(), gp.getPos2().getX(), gp.getPos2().getZ(), player.getWorld().getName());
                    AywenCraftPlugin.getInstance().regions.add(region);

                    MessageManager.sendMessageType(player, "§aVous venez de créer une nouvelle région.", Prefix.CLAIM, MessageType.SUCCESS, true);

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


    // Check region
    private void checkRegion(Player player, Block block, Event eventHandler) {
        AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
        for (RegionManager region : plugin.regions) {
            if (region.isInArea(block.getLocation())) {
                boolean isBypassing = AywenCraftPlugin.playerClaimsByPass.contains(player);
                boolean isTeamMember = region.isTeamMember(player.getUniqueId());

                if (!isBypassing && !isTeamMember) {
                    MessageManager.sendMessageType(player, "§cCe n'est pas chez vous", Prefix.CLAIM, MessageType.ERROR, true);
                    if (eventHandler instanceof Cancellable cancellable) cancellable.setCancelled(true);
                    return;
                }
            }
        }
    }

    // Check region if player is null
    public void checkRegion(Block block, Event eventHandler) {
        AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
        for (RegionManager region : plugin.regions) {
            if (region.isInArea(block.getLocation())) {
                if (eventHandler instanceof Cancellable cancellable) {
                    cancellable.setCancelled(true);
                }
            }
        }
    }

    // Check if explosion is in region
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
}