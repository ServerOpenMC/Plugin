package fr.communaywen.core.personalhome.listeners;

import fr.communaywen.core.personalhome.HomesUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BuildRestrictions implements Listener {
    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        if (event.getBlock().getWorld().getName().equals("homes")) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }
    
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if (!event.getBlock().getWorld().getName().equals("homes")) { return; }
        Player player = event.getPlayer();
        if (player.hasPermission("ayw.maisons.bypass")) { return; }

        if (event.getBlock().getX() < 0) { return; }

        if (HomesUtils.isOutOfHome(event.getBlock().getLocation())) {
            player.sendMessage("§cVous avez dépasser les bordures de votre maison.");
            event.setCancelled(true);
            return;
        }

        if (HomesUtils.isntInHisHome(player, event.getBlock().getLocation())) {
            player.sendMessage("§cVous n'êtes pas chez vous!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getWorld().getName().equals("homes")) { return; }
        Player player = event.getPlayer();
        if (player.hasPermission("ayw.maisons.bypass")) { return; }

        if (HomesUtils.isOutOfHome(event.getBlock().getLocation())) {
            player.sendMessage("§cVous avez dépasser les bordures de votre maison.");
            event.setCancelled(true);
            return;
        }

        if (HomesUtils.isntInHisHome(player, event.getBlock().getLocation())) {
            player.sendMessage("§cVous n'êtes pas chez vous!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("homes")) { return; }
        if (player.hasPermission("ayw.maisons.bypass")) { return; }

        try {
            if (HomesUtils.isntInHisHome(player, event.getClickedBlock().getLocation())) {
                player.sendMessage("§cVous n'êtes pas chez vous!");
                event.setCancelled(true);
                return;
            }
        } catch (Exception e) {
            // Surement parceque event.getClickedBlock() est null
        }
    }
}
