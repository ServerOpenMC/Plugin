package fr.communaywen.core.commands.randomEvents;

import fr.communaywen.core.utils.database.DatabaseConnector;
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


// Stocke les préférences des joueurs en terme de difficulté etc
public class RandomEventsData extends DatabaseConnector {
    private static Map<UUID, Integer> playersDifficulties;

    public static void setPlayerDifficulty(Player player, Integer difficulty) {
        if (0 <= difficulty && difficulty <= 3) {
            playersDifficulties.put(player.getUniqueId(), difficulty);
            savePlayerData(player);
        }
    }
    public static Integer getPlayerDifficulty(Player player) {
        return playersDifficulties.getOrDefault(player.getUniqueId(), EventsDifficulties.NORMAL);
    }

    // returns true on success, false when it fails
    public static boolean savePlayerData(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM random_events WHERE player = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                statement = connection.prepareStatement("UPDATE random_events SET difficulty = ? WHERE player = ?");
                statement.setDouble(1, getPlayerDifficulty(player));
                statement.setString(2, player.getUniqueId().toString());
            } else {
                statement = connection.prepareStatement("INSERT INTO random_events (player, difficulty) VALUES (?, ?)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setDouble(2, getPlayerDifficulty(player));
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

        } catch (SQLException ignored) {
            return false;
        }
        return true;
    }

    public static void loadData() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM random_events");
            ResultSet result = statement.executeQuery();

            playersDifficulties = new HashMap<>();

            while (result.next()) {
                playersDifficulties.put(UUID.fromString(result.getString("player")), result.getInt("difficulty"));
            }
        } catch (SQLException ignored) {
            System.err.println("Random Events data coundn't be loaded !");
        }
    }
}
    