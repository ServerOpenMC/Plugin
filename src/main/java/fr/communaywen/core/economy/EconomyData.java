package fr.communaywen.core.economy;

import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyData extends DatabaseConnector {
    public static void saveBalances(Player player, Map<UUID, Double> balances) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM economie WHERE player = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();
            PreparedStatement stm;
            if (rs.next()) {
                stm = connection.prepareStatement("UPDATE economie SET balance = ? WHERE player = ?");
                stm.setDouble(1, balances.getOrDefault(player.getUniqueId(), 0.0));
                stm.setString(2, player.getUniqueId().toString());
            } else {
                stm = connection.prepareStatement("INSERT INTO economie (player, balance) VALUES (?, ?)");
                stm.setString(1, player.getUniqueId().toString());
                stm.setDouble(2, balances.getOrDefault(player.getUniqueId(), 0.0));
            }

            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving balance failed, no rows affected.");
            }

        } catch (SQLException ignored) {}
    }

    public static void saveBalancesOffline(OfflinePlayer player, Map<UUID, Double> balances) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM economie WHERE player = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();
            PreparedStatement stm;
            if (rs.next()) {
                stm = connection.prepareStatement("UPDATE economie SET balance = ? WHERE player = ?");
                stm.setDouble(1, balances.getOrDefault(player.getUniqueId(), 0.0));
                stm.setString(2, player.getUniqueId().toString());
            } else {
                stm = connection.prepareStatement("INSERT INTO economie (player, balance) VALUES (?, ?)");
                stm.setString(1, player.getUniqueId().toString());
                stm.setDouble(2, balances.getOrDefault(player.getUniqueId(), 0.0));
            }

            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving balance failed, no rows affected.");
            }

        } catch (SQLException ignored) {}
    }

    public static Map<UUID, Double> loadBalances() {
        try {
            Map<UUID, Double> balances = new HashMap<>();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM economie");
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                while(rs.next()) {
                    balances.put(UUID.fromString(rs.getString("player")), rs.getDouble("balance"));
                }
            }

            return balances;
        } catch (SQLException ignored) {}
        return null;
    }
}
