package fr.communaywen.core.listeners;

import fr.communaywen.core.friends.FriendsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class FriendsListener implements Listener {

    private FriendsManager friendsManager;

    public FriendsListener(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        friendsManager.getFriendsAsync(p.getUniqueId().toString()).thenAccept(friendsUUIDs -> {
            for (String friendUUID : friendsUUIDs) {
                Player friend = Bukkit.getPlayer(UUID.fromString(friendUUID));

                if (friend != null && friend.isOnline()) {
                    friend.sendMessage("§aVotre ami §e" + p.getName() + " §as'est connecté(e) !");
                }
            }
        }).exceptionally(ex -> {
            System.out.println("Erreur lors de la récupération des amis : " + ex.getMessage());
            return null;
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        friendsManager.getFriendsAsync(p.getUniqueId().toString()).thenAccept(friendsUUIDs -> {
            for (String friendUUID : friendsUUIDs) {
                Player friend = Bukkit.getPlayer(UUID.fromString(friendUUID));

                if (friend != null && friend.isOnline()) {
                    friend.sendMessage("§cVotre ami §e" + p.getName() + " §cs'est déconnecté(e) !");
                }
            }
        }).exceptionally(ex -> {
            System.out.println("Erreur lors de la récupération des amis : " + ex.getMessage());
            return null;
        });
    }

}
