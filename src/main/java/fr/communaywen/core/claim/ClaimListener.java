package fr.communaywen.core.claim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.EconomieTeam;
import fr.communaywen.core.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClaimListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new GamePlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
            if (region.isInArea(event.getBlock().getLocation()) && !region.isTeamMember(playerUuid)) {
                event.setCancelled(true);
                player.sendMessage("§cCe n'est pas chez vous");
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
            if (region.isInArea(event.getBlock().getLocation()) && !region.isTeamMember(playerUuid)) {
                event.setCancelled(true);
                player.sendMessage("§cCe n'est pas chez vous");
                return;
            }
        }
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

    // Check if player craft with claim stick
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                if (region.isInArea(event.getClickedBlock().getLocation()) && !region.isTeamMember(playerUuid)) {
                    event.setCancelled(true);
                    player.sendMessage("§cCe n'est pas chez vous");
                }
            }
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            ItemStack item = player.getItemInHand();
            if (player.getItemInHand().getType() == Material.STICK && item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                event.setCancelled(true);

                GamePlayer gp = GamePlayer.gamePlayers.get(player.getName());
                Team playerTeam = AywenCraftPlugin.getInstance().getManagers().getTeamManager().getTeamByPlayer(playerUuid);

                if (gp.getPos1() == null) {

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

    private void removeClaimStick(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.STICK && Objects.requireNonNull(item.getItemMeta()).hasDisplayName() && item.getItemMeta().getDisplayName().equals("§cBATON DE CLAIM")) {
                player.getInventory().remove(item);
            }
        }
    }
}
