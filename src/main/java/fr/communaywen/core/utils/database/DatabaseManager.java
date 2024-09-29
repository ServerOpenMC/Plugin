package fr.communaywen.core.utils.database;

import fr.communaywen.core.AywenCraftPlugin;
import lombok.Getter;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseManager {

    private DatabaseConnection connection;
    private final AywenCraftPlugin plugin;

    @Getter
    private final boolean enabled;

    public DatabaseManager(AywenCraftPlugin plugin, boolean enabled) {
        this.plugin = plugin;
        this.enabled = enabled;

        if (!enabled) {
            return;
        }
        if (plugin.getConfig().getString("database.url") == null) {
            plugin.getLogger().severe("\n\nPlease, add the database configuration in the config.yml file !\n\n");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        this.connection = new DatabaseConnection(plugin.getConfig().getString("database.url"),
                plugin.getConfig().getString("database.user"), plugin.getConfig().getString("database.password"));
    }

    public void init() throws SQLException {
        if (!enabled) {
            return;
        }
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS friends (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "firstPlayer_uuid VARCHAR(36) NOT NULL," +
                "secondPlayer_uuid VARCHAR(36) NOT NULL," +
                "friendDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS mailbox_items (" +
                                                      "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                                      "sender_id VARCHAR(36) NOT NULL," +
                                                      "receiver_id VARCHAR(36) NOT NULL," +
                                                      "items BLOB NOT NULL," +
                                                      "items_count INT NOT NULL," +
                                                      "sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                                                      "refused BOOLEAN NOT NULL DEFAULT FALSE" +
                                                      ")").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS blacklists (Owner VARCHAR(36), Blocked VARCHAR(36))").executeUpdate();

        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS personal_homes (id SMALLINT unsigned NOT NULL PRIMARY KEY, player VARCHAR(36), spawnpoint VARCHAR(255) DEFAULT NULL, biome VARCHAR(255) DEFAULT 'PLAINS', allow_visit BOOLEAN DEFAULT false)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS link (discord_id VARCHAR(100) NOT NULL, minecraft_uuid VARCHAR(36))").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS link_verif (minecraft_uuid VARCHAR(36) NOT NULL, code int(11) NOT NULL)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS events_rewards (player VARCHAR(36) NOT NULL PRIMARY KEY, scope VARCHAR(32) NOT NULL, isClaimed BOOLEAN)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS teams_player (teamName VARCHAR(16) NOT NULL, player VARCHAR(36) NOT NULL)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS teams (teamName VARCHAR(16) NOT NULL PRIMARY KEY, owner VARCHAR(36) NOT NULL, balance BIGINT UNSIGNED, inventory LONGBLOB)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS transactions (recipient VARCHAR(36), sender VARCHAR(36), amount DOUBLE, reason VARCHAR(255), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)").executeUpdate();
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS advancements (player VARCHAR(36) , advancement VARCHAR(255), value INT, PRIMARY KEY (player, advancement))").executeUpdate();


        //Système de signalements

        this.getConnection().prepareStatement("CREATE TABLE `reports` (`sender` text NOT NULL,`reported` text NOT NULL,`reason` text NOT NULL,`timestamp` timestamp NOT NULL)");

        // Système de claims
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS claim (" +
                "  claimID varchar(36) NOT NULL PRIMARY KEY," +
                "  team varchar(16) NOT NULL," +
                "  pos1x double NOT NULL," +
                "  pos1z double NOT NULL," +
                "  pos2x double NOT NULL," +
                "  pos2z double NOT NULL," +
                "  world varchar(20) NOT NULL," +
                "  claimer varchar(36) NOT NULL" +
                ")").executeUpdate();

        // Système d'économie
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS economie (" +
                "  player varchar(36) NOT NULL PRIMARY KEY," +
                "  balance double NOT NULL" +
                ")").executeUpdate();

        // Ėvènements aléatoires
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS random_events (" +
                "  player varchar(36) NOT NULL PRIMARY KEY," +
                "  difficulty TINYINT NOT NULL" +
                ")").executeUpdate();

        // Système de Contest
        // table prog contest
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS contest (phase int(11), camp1 VARCHAR(36), color1 VARCHAR(36), camp2 VARCHAR(36), color2 VARCHAR(36), startdate VARCHAR(36), points1 int(11), points2 int(11))").executeUpdate();
        PreparedStatement state = connection.getConnection().prepareStatement("SELECT COUNT(*) FROM contest");
        ResultSet rs = state.executeQuery();

        // push first contest
        if(rs.next()) {
            if(rs.getInt(1) == 0) {
                PreparedStatement states = this.getConnection().prepareStatement("INSERT INTO contest (phase, camp1, color1, camp2, color2, startdate, points1, points2) VALUES (1, 'Mayonnaise', 'YELLOW', 'Ketchup', 'RED', ?, 0,0)");

                String dateContestStart = "ven.";
                states.setString(1, dateContestStart);
                states.executeUpdate();
            }
        }

        // Table camps
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS camps (minecraft_uuid VARCHAR(36), name VARCHAR(36), camps int(11), point_dep int(11))").executeUpdate();

        // Système de homes
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS homes (" +
                "  player varchar(36) NOT NULL," +
                "  name varchar(10) NOT NULL," +
                "  location mediumtext NOT NULL" +
                ")").executeUpdate();

        // Système de players
        this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players (" +
                "player VARCHAR(36) NOT NULL PRIMARY KEY," +
                "homes_limit INT NOT NULL DEFAULT 1" +
                ")").executeUpdate();

        this.getConnection().prepareStatement("ALTER TABLE claim ADD COLUMN IF NOT EXISTS claimer VARCHAR(36) NOT NULL").executeUpdate();


        System.out.println("Les tables ont été créer si besoin");

    }

    public void close() {
        if (!enabled) {
            return;
        }
        try {
            this.connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred while closing the database connection !");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (!enabled) {
            return null;
        }
        try {
            return connection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void register(Class<?>... classes) {
        if (!enabled) {
            return;
        }
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
