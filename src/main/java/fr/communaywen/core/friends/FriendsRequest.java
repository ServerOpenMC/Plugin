package fr.communaywen.core.friends;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsRequest extends BukkitRunnable {

    private final FriendsManager friendsManager;
    private final AywenCraftPlugin plugin;
    private final List<String> uuids = new ArrayList<>();

    public FriendsRequest(FriendsManager friendsManager, AywenCraftPlugin plugin, String firstUUID, String secondUUID) {
        this.friendsManager = friendsManager;
        this.plugin = plugin;
        this.uuids.add(firstUUID);
        this.uuids.add(secondUUID);
    }

    public boolean containsUUID(String uuid) {
        return uuids.contains(uuid);
    }

    public void sendRequest() {
        this.runTaskLater(plugin, 6000L);
    }

    private void removeRequest() {
        sendExpiryMessage(uuids.get(0));
        sendExpiryMessage(uuids.get(1));
        friendsManager.removeRequest(this);
        uuids.clear();
    }

    private void sendExpiryMessage(String playerUUID) {
        if (isPlayerOnline(playerUUID)) {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (player != null) {
                player.sendMessage("§cLa requête a été refusée ou a expiré.");
            }
        }
    }

    @Override
    public void run() {
        removeRequest();
    }

    private boolean isPlayerOnline(String playerUUID) {
        return Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).isOnline();
    }
}
