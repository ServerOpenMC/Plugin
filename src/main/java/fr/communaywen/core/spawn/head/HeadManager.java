package fr.communaywen.core.spawn.head;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HeadManager extends DatabaseConnector  {
    static AywenCraftPlugin plugin;
    static FileConfiguration config;

    public HeadManager(AywenCraftPlugin plugins) {
        config = plugins.getConfig();
        plugin = plugins;
    }

    public static void saveFoundHead(Player player, String headId) {
        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT heads FROM spawn_head WHERE uuid = ?");
            selectStatement.setString(1, player.getUniqueId().toString());
            ResultSet rs = selectStatement.executeQuery();

            JSONArray headsArray = new JSONArray();

            if (rs.next()) {
                String heads = rs.getString("heads");
                if (heads != null && !heads.isEmpty()) {
                    headsArray = new JSONArray(heads);
                }
            } else {
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO spawn_head (uuid, heads) VALUES (?, ?)");
                insertStatement.setString(1, player.getUniqueId().toString());
                insertStatement.setString(2, "[]");
                insertStatement.executeUpdate();
            }

            if (!headsArray.toList().contains(headId)) {
                headsArray.put(headId);

                String sql = "UPDATE spawn_head SET heads = ? WHERE uuid = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
                    updateStatement.setString(1, headsArray.toString());
                    updateStatement.setString(2, player.getUniqueId().toString());
                    updateStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getFoundHeads(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT heads FROM spawn_head WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String heads = rs.getString("heads");
                JSONArray headsArray = new JSONArray(heads);
                return headsArray.toList().stream().map(Object::toString).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public static Integer getNumberHeads(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT heads FROM spawn_head WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String heads = rs.getString("heads");
                JSONArray headsArray = new JSONArray(heads);
                return headsArray.toList().stream().map(Object::toString).toList().size();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Integer getMaxHeads() {
        List<Map<?, ?>> headList = config.getMapList("head.list");

        return headList.size();
    }

    public static boolean hasFoundHead(Player player, String headId) {
        List<String> foundHeads = getFoundHeads(player);
        return foundHeads.contains(headId);
    }
}
