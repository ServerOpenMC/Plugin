package fr.communaywen.core.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

import fr.communaywen.core.AywenCraftPlugin;

public class DatabaseManager {
    private DatabaseConnection connection;
    private AywenCraftPlugin plugin;

    public DatabaseManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getString("database.url") == null) {
            plugin.getLogger().severe("\n\nPlease, add the database configuration in the config.yml file !\n\n");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        this.connection = new DatabaseConnection(plugin.getConfig().getString("database.url"),
                plugin.getConfig().getString("database.user"), plugin.getConfig().getString("database.password"));
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred while closing the database connection !");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            return connection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
