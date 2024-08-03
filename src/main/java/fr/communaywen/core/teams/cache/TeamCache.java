package fr.communaywen.core.teams.cache;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TeamCache {

    private final AywenCraftPlugin plugin;
    private final Map<String, List<UUID>> teamCache = new HashMap<>();

    public TeamCache(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        startAutoSaveTask();
    }

    public void addPlayer(String teamName, UUID playerUUID) {
        teamCache.computeIfAbsent(teamName, k -> new ArrayList<>()).add(playerUUID);
    }

    public void removePlayer(String teamName, UUID playerUUID) {
        List<UUID> players = teamCache.get(teamName);
        if (players != null) {
            players.remove(playerUUID);
            if (players.isEmpty()) {
                teamCache.remove(teamName);
            }
        }
    }

    public List<UUID> getTeamPlayers(String teamName) {
        return teamCache.getOrDefault(teamName, new ArrayList<>());
    }

    public Map<String, List<UUID>> getTeams() {
        return teamCache;
    }

    private void startAutoSaveTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveCacheToDatabase();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 36000);
    }

    public void saveCacheToDatabase() {
        try (Connection connection = plugin.getManagers().getDatabaseManager().getConnection()) {
            for (Map.Entry<String, List<UUID>> entry : teamCache.entrySet()) {
                String teamName = entry.getKey();
                List<UUID> players = entry.getValue();

                PreparedStatement clearStatement = connection.prepareStatement("DELETE FROM teams_player WHERE teamName = ?");
                clearStatement.setString(1, teamName);
                clearStatement.executeUpdate();

                for (UUID player : players) {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO teams_player (teamName, player) VALUES (?, ?)");
                    statement.setString(1, teamName);
                    statement.setString(2, player.toString());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAllTeamsToDatabase() {
        saveCacheToDatabase();
    }
}
