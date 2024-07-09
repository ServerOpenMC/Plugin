package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
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

    public FriendsListener(FriendsManager friendsManager){
        this.friendsManager = friendsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();

        for(String friends_uuid : friendsManager.getFriends(p.getUniqueId().toString())){
            Player friends = Bukkit.getPlayer(UUID.fromString(friends_uuid));

            if(friends != null && friends.isOnline()) {
                friends.sendMessage("§aVotre ami §e" + p.getName() + " §as'est connecté(e) !");
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) throws SQLException {
        Player p = e.getPlayer();

        for(String friends_uuid : friendsManager.getFriends(p.getUniqueId().toString())){
            Player friends = Bukkit.getPlayer(UUID.fromString(friends_uuid));

            if(friends != null && friends.isOnline()){
                friends.sendMessage("§cVotre ami §e" + p.getName() + " §cs'est déconnecté(e) !");
            }
        }
    }

}
