package fr.communaywen.core.utils.database;

import java.sql.Connection;

public class DatabaseConnector {
    protected static Connection connection;
    public static void setConnection(Connection connection) {
        DatabaseConnector.connection = connection;
    }
}
