package fr.communaywen.core.utils.database;

import fr.communaywen.core.AywenCraftPlugin;

import java.sql.SQLException;

public class DatabaseManager {
    private DatabaseConnection teamConnection;
    private AywenCraftPlugin plugin;

    public DatabaseManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        if(plugin.getConfig().getString("database.url") == null) {
            plugin.getLogger().severe("\n\nPlease, add the database configuration in the config.yml file !\n\n");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        this.teamConnection = new DatabaseConnection(plugin.getConfig().getString("database.url"), plugin.getConfig().getString("database.user"), plugin.getConfig().getString("database.password"));
    }

    public void close() {
        try {
            this.teamConnection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred while closing the database connection !");
            e.printStackTrace();
        }
    }

    public DatabaseConnection getTeamConnection() {
        return teamConnection;
    }
}
