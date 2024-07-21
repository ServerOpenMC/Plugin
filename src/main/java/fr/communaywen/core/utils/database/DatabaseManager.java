package fr.communaywen.core.utils.database;

import fr.communaywen.core.AywenCraftPlugin;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void init() throws SQLException {
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS friends (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "firstPlayer_uuid VARCHAR(36) NOT NULL," +
                "secondPlayer_uuid VARCHAR(36) NOT NULL," +
                "friendDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")").executeUpdate();

        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS blacklists (Owner VARCHAR(36), Blocked VARCHAR(36))").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS link (discord_id VARCHAR(100) NOT NULL, minecraft_uuid VARCHAR(36))").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS events_rewards (player VARCHAR(36) NOT NULL PRIMARY KEY, scope VARCHAR(32) NOT NULL, isClaimed BOOLEAN)").executeUpdate();

        // Système de claims
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS claim (" +
                "  team varchar(16) NOT NULL," +
                "  pos1x double NOT NULL," +
                "  pos1z double NOT NULL," +
                "  pos2x double NOT NULL," +
                "  pos2z double NOT NULL," +
                "  world varchar(20) NOT NULL" +
                ")").executeUpdate();
        System.out.println("Les tables ont été créer si besoin");
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

    public void register(Class<?>... classes) {
        for (Class<?> dbClass : classes) {
            if (DatabaseConnector.class.isAssignableFrom(dbClass)) {
                try {
                    Method setConnectionMethod = dbClass.getMethod("setConnection", Connection.class);
                    setConnectionMethod.invoke(null, getConnection());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
