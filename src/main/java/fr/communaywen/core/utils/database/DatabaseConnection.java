package fr.communaywen.core.utils.database;

import fr.communaywen.core.AywenCraftPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final String url;
    private final String user;
    private final String password;

    private Connection connection;

    public DatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        this.connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            AywenCraftPlugin.getInstance().getLogger().info("\u001B[32m" + "Connexion à la base de données réussie");
        } catch (SQLException | ClassNotFoundException e) {
            AywenCraftPlugin.getInstance().getLogger().warning("\u001B[31m" + "Connexion à la base de données échouée");
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                try {
                    this.connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                return this.connection;
            }
        }
        this.connect();
        return this.connection;
    }
}