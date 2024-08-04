package fr.communaywen.core.tpa;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class TPAQueue {
    public static final TPAQueue INSTANCE = new TPAQueue();

    private TPAQueue() {
    }

    private final ConcurrentHashMap<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Long> tpaRequestTimes = new ConcurrentHashMap<>();

    public boolean hasPendingRequest(Player player) {
        return tpaRequests.containsKey(player.getUniqueId());
    }

    public void addRequest(Player player, Player target) {
        tpaRequests.put(target.getUniqueId(), player.getUniqueId());
        tpaRequestTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void expireRequest(Player player, Player target) {
        if (tpaRequests.containsKey(target.getUniqueId())) {
            long requestTime = tpaRequestTimes.get(player.getUniqueId());
            if (System.currentTimeMillis() - requestTime >= 120000) { // 2 minutes
                player.sendMessage("§cVotre demande de téléportation à §f" + target.getName() + " §ca expiré.");
                tpaRequests.remove(target.getUniqueId());
                tpaRequestTimes.remove(player.getUniqueId());
            }
        }
    }

    public Player getRequester(Player target) {
        UUID requesterUUID = tpaRequests.get(target.getUniqueId());
        return requesterUUID == null ? null : target.getServer().getPlayer(requesterUUID);
    }

    public void removeRequest(Player target) {
        tpaRequests.remove(target.getUniqueId());
    }
}

