package fr.communaywen.core.utils.database;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Blacklist extends DatabaseConnector {
    public static boolean isBlacklisted(Player plrA, Player plrB) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM blacklists WHERE Owner = ? AND Blocked = ?");

            statement.setString(1, plrA.getUniqueId().toString());
            statement.setString(2, plrB.getUniqueId().toString());

            ResultSet result = statement.executeQuery();

            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<String> getBlacklist(Player player) throws SQLException {
        List<String> blacklist = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM blacklists WHERE Owner = ?");

        statement.setString(1, player.getUniqueId().toString());

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            String blacklistedPlayer = result.getString("Blocked");
            blacklist.add(blacklistedPlayer);
        }

        return blacklist;
    }

    public static void block(Player blocker, OfflinePlayer player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO blacklists VALUES (?, ?)");
        statement.setString(1, blocker.getUniqueId().toString());
        statement.setString(2, player.getUniqueId().toString());
        statement.executeUpdate();
    }

    public static void unblock(Player blocker, OfflinePlayer player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM blacklists WHERE Owner=? AND Blocked=?");
        statement.setString(1, blocker.getUniqueId().toString());
        statement.setString(2, player.getUniqueId().toString());
        statement.executeUpdate();
    }
}
