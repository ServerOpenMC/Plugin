package fr.communaywen.core.friends;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.FriendsUtils;
import fr.communaywen.core.utils.database.DatabaseManager;
import lombok.Getter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Feature("Amis")
@Credit("Martinouxx")
@Collaborators("Xernas")
public class FriendsManager {

    private final DatabaseManager dbManager;
    private final AywenCraftPlugin plugin;

    @Getter
    private final List<FriendsRequest> friendsRequests = new ArrayList<>();

    public FriendsManager(DatabaseManager dbManager, AywenCraftPlugin plugin) {
        this.dbManager = dbManager;
        this.plugin = plugin;
    }

    public CompletableFuture<List<String>> getFriendsAsync(String playerUUID) {
        return FriendsUtils.getAllFriendsAsync(dbManager, playerUUID);
    }

    public void addFriend(String firstUUID, String secondUUID) throws SQLException {
        FriendsUtils.addInDatabase(dbManager, firstUUID, secondUUID);
        removeRequest(getRequest(firstUUID));
    }

    public void removeFriend(String firstUUID, String secondUUID) throws SQLException {
        FriendsUtils.removeInDatabase(dbManager, firstUUID, secondUUID);
    }

    public boolean areFriends(String firstUUID, String secondUUID) throws SQLException {
        return FriendsUtils.areFriends(dbManager, firstUUID, secondUUID);
    }

    public Timestamp getTimestamp(String firstUUID, String secondUUID) throws SQLException {
        return FriendsUtils.getTimestamp(dbManager, firstUUID, secondUUID);
    }

    public void addRequest(String firstUUID, String secondUUID) {
        if (isRequestPending(firstUUID)) {
            return;
        }

        FriendsRequest friendsRequest = new FriendsRequest(this, plugin, firstUUID, secondUUID);
        friendsRequest.sendRequest();
        friendsRequests.add(friendsRequest);
    }

    public void removeRequest(FriendsRequest friendsRequest) {
        if (friendsRequest != null) {
            if (!friendsRequest.isCancelled()) {
                friendsRequest.cancel();
            }
        }

        friendsRequests.remove(friendsRequest);
    }

    public FriendsRequest getRequest(String uuid) {
        for (FriendsRequest friendsRequests : friendsRequests) {
            if (friendsRequests.containsUUID(uuid)) {
                return friendsRequests;
            }
        }
        return null;
    }

    public boolean isRequestPending(String uuid) {
        return friendsRequests.stream().anyMatch(request -> request.containsUUID(uuid));
    }

}
