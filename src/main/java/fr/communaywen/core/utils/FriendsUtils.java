package fr.communaywen.core.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseManager;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendsUtils {

    private static final String TABLE_NAME = "friends";

    public static boolean addInDatabase(DatabaseManager dbManager, String firstUUID, String secondUUID) throws SQLException {
        try {
            Connection connection = dbManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (firstPlayer_uuid, secondPlayer_uuid, friendDate) VALUES (?, ?, ?)");

            statement.setString(1, firstUUID);
            statement.setString(2, secondUUID);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static boolean removeInDatabase(DatabaseManager dbManager, String firstUUID, String secondUUID) throws SQLException {
        try {
            Connection connection = dbManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE firstPlayer_uuid = ? AND secondPlayer_uuid = ?");

            statement.setString(1, firstUUID);
            statement.setString(2, secondUUID);

            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static boolean areFriends(DatabaseManager dbManager, String firstUUID, String secondUUID) throws SQLException {
        try {
            Connection connection = dbManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE (firstPlayer_uuid = ? AND secondPlayer_uuid = ?) OR (firstPlayer_uuid = ? AND secondPlayer_uuid = ?)");

            statement.setString(1, firstUUID);
            statement.setString(2, secondUUID);
            statement.setString(3, secondUUID);
            statement.setString(4, firstUUID);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static Timestamp getTimestamp(DatabaseManager dbManager, String firstUUID, String secondUUID) throws SQLException {
        try {
            Connection connection = dbManager.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT friendDate FROM " + TABLE_NAME + " WHERE (firstPlayer_uuid = ? AND secondPlayer_uuid = ?) OR (firstPlayer_uuid = ? AND secondPlayer_uuid = ?)");

            statement.setString(1, firstUUID);
            statement.setString(2, secondUUID);
            statement.setString(3, secondUUID);
            statement.setString(4, firstUUID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTimestamp("friendDate");
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;
    }

    public static CompletableFuture<List<String>> getAllFriendsAsync(DatabaseManager dbManager, String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> friends = new ArrayList<>();

            try {
                Connection connection = dbManager.getConnection();

                String sql = "SELECT * FROM " + TABLE_NAME + " WHERE firstPlayer_uuid = ? OR secondPlayer_uuid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, uuid);
                statement.setString(2, uuid);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String friendUUID = resultSet.getString("firstPlayer_uuid").equals(uuid) ? resultSet.getString("secondPlayer_uuid") : resultSet.getString("firstPlayer_uuid");
                    friends.add(friendUUID);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return friends;
        });
    }

}
