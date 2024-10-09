package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.FreezeUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FreezeListener implements Listener {

    private final AywenCraftPlugin plugin;

    public FreezeListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (plugin.getFrozenPlayers().contains(playerUUID)) {
            int banDurationDays = plugin.getBanDuration();
            Date banDuration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(banDurationDays));
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Déconnexion en étant freeze !", banDuration, "Anti Déco Freeze");
            plugin.getFrozenPlayers().remove(playerUUID);

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (AywenCraftPlugin.frozenPlayers.contains(player)) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player && AywenCraftPlugin.frozenPlayers.contains(entity)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (AywenCraftPlugin.frozenPlayers.contains(player)) {
            e.setCancelled(true);
        }
    }
}
