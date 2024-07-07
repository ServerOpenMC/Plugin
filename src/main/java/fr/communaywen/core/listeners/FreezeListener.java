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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        if (plugin.frozenPlayers.contains(player)) {
            e.setTo(e.getFrom());
            player.sendMessage(FreezeUtils.prefix + ChatColor.DARK_RED + "Vous êtes freeze !");
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player && plugin.frozenPlayers.contains(entity)) {
            e.setCancelled(true);
        }
    }
}
