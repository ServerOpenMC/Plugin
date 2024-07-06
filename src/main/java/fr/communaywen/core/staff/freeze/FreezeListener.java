package fr.communaywen.core.staff.freeze;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.http.WebSocket;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FreezeListener implements WebSocket.Listener {

    private final AywenCraftPlugin plugin;

    public FreezeListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getFrozenPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (plugin.getFrozenPlayers().contains(playerUUID)) {
            int banDurationDays = plugin.getBanDuration();
            Date banDuration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(banDurationDays));
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Vous avez été banni pour déconnexion en étant freeze pandant ", banDuration, "Anti Déco Freeze");
            plugin.getFrozenPlayers().remove(playerUUID);

        }
    }
}
