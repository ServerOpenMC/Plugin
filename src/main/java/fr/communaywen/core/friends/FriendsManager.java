package fr.communaywen.core.friends;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.FriendsUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FriendsManager {

    private final FriendsUtils friendsUtils;
    private final AywenCraftPlugin plugin;
    private final List<FriendsRequest> friendsRequests = new ArrayList<>();

    public FriendsManager(FriendsUtils friendsUtils, AywenCraftPlugin plugin) {
        this.friendsUtils = friendsUtils;
        this.plugin = plugin;
    }

    public List<String> getFriends(String playerUUID) throws SQLException {
        return friendsUtils.getAllFriends(playerUUID);
    }

    public void addFriend(String firstUUID, String secondUUID) throws SQLException {
        friendsUtils.addInDatabase(firstUUID, secondUUID);
        removeRequest(getRequest(firstUUID));
    }

    public void removeFriend(String firstUUID, String secondUUID) throws SQLException {
        friendsUtils.removeInDatabase(firstUUID, secondUUID);
    }

    public boolean areFriends(String firstUUID, String secondUUID) throws SQLException {
        return friendsUtils.areFriends(firstUUID, secondUUID);
    }

    public Timestamp getTimestamp(String firstUUID, String secondUUID) throws SQLException {
        return friendsUtils.getTimestamp(firstUUID, secondUUID);
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
        if(friendsRequest != null) {
            if (!friendsRequest.isCancelled()) {
                friendsRequest.cancel();
            }
        }

        friendsRequests.remove(friendsRequest);
    }

    public FriendsRequest getRequest(String uuid){
        for(FriendsRequest friendsRequests : friendsRequests){
            if(friendsRequests.containsUUID(uuid)){
                return friendsRequests;
            }
        }
        return null;
    }

    public boolean isRequestPending(String uuid) {
        return friendsRequests.stream().anyMatch(request -> request.containsUUID(uuid));
    }

    public List<FriendsRequest> getFriendsRequests() {
        return friendsRequests;
    }
}
