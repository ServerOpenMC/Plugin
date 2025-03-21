package fr.communaywen.core.economy;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
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

    public static void saveBalances(UUID player, Map<UUID, Double> balances) {
        Bukkit.getScheduler().runTaskAsynchronously(AywenCraftPlugin.getInstance(), () -> {
          try {
              PreparedStatement statement = connection.prepareStatement("SELECT * FROM economie WHERE player = ?");
              statement.setString(1, player.toString());

              ResultSet rs = statement.executeQuery();
              PreparedStatement stm;
              if (rs.next()) {
                  stm = connection.prepareStatement("UPDATE economie SET balance = ? WHERE player = ?");
                  stm.setDouble(1, balances.getOrDefault(player, 0.0));
                  stm.setString(2, player.toString());
              } else {
                  stm = connection.prepareStatement("INSERT INTO economie (player, balance) VALUES (?, ?)");
                  stm.setString(1, player.toString());
                  stm.setDouble(2, balances.getOrDefault(player, 0.0));
              }
              int affectedRows = stm.executeUpdate();

              if (affectedRows == 0) {
                  throw new SQLException("Saving balance failed, no rows affected.");
              }

            } catch (SQLException ignored) {
            }
        });
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
