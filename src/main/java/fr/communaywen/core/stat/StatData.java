package fr.communaywen.core.stat;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatData extends DatabaseConnector {

    public static Map<UUID, Stats> loadStats() {
        try {
            Map<UUID, Stats> stats = new HashMap<>();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Map<Stats.StatList,Number> map = new HashMap<>();
                for (Stats.StatList value : Stats.StatList.values()) {
                    map.put(value, (Number) rs.getObject(value.getName()));
                }
                stats.put(UUID.fromString(rs.getString("player")),new Stats(map));
            }
            System.out.println("data loaded");
            return stats;

        } catch (SQLException ignored) {}

        return null;
    }

    public static Map<UUID, Stats> loadStats(Player player) {
        try {
            Map<UUID, Stats> stats = new HashMap<>();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Map<Stats.StatList,Number> map = new HashMap<>();
                for (Stats.StatList value : Stats.StatList.values()) {
                    map.put(value, (Number) rs.getObject(value.getName()));
                }
                stats.put(UUID.fromString(rs.getString("player")),new Stats(map));
            }

            return stats;

        } catch (SQLException ignored) {}

        return null;
    }
    public static void saveStats(Player player) {
        Stats stats = AywenCraftPlugin.getInstance().getManagers().getStatsManager().getStats(player);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats WHERE player = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();
            PreparedStatement stm;
            //update if exist else insert
            if (rs.next()) {
                StringBuilder builder = new StringBuilder("UPDATE stats SET ");
                for (Stats.StatList value : stats.getValues().keySet()) {
                    builder.append(value.getName()).append(" = ").append(stats.getValues().get(value)).append(", ");
                }
                builder.deleteCharAt(builder.length()-2);
                builder.append(" WHERE player = \"").append(player.getUniqueId()).append("\"");
                stm = connection.prepareStatement(
                        builder.toString());
                stm.executeUpdate();

            } else {
                StringBuilder mainBuilder = new StringBuilder("INSERT INTO stats (player");
                StringBuilder valueBuilder = new StringBuilder("( \"").append(player.getUniqueId()).append("\"");
                for (Stats.StatList value : stats.getValues().keySet()) {
                    mainBuilder.append(", ").append(value.getName());
                    valueBuilder.append(", ").append(stats.getValues().get(value));
                }
                valueBuilder.append(" )");
                mainBuilder.append(") VALUES (").append(valueBuilder);
                stm = connection.prepareStatement(
                        mainBuilder.toString());
                stm.executeUpdate();
            }
            System.out.println("data save");
            int affectedRows = stm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving stats failed, no rows affected.");
            }

        } catch (SQLException ignored) {
           // System.out.println(ignored.toString());
        }
    }
}
