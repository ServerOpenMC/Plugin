package fr.communaywen.core.utils.database;

import org.bukkit.Location;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Waypoints extends DatabaseConnector {

    public static boolean addWaypoint(String playerUUID, String waypointName, Location location) {

        // Round at 2 decimals MAX
        double x = Math.round(location.getX() * 100.0) / 100.0;
        double y = Math.round(location.getY() * 100.0) / 100.0;
        double z = Math.round(location.getZ() * 100.0) / 100.0;
        World world = location.getWorld();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO waypoints VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, playerUUID);
            statement.setString(2, waypointName);
            statement.setDouble(3, x);
            statement.setDouble(4, y);
            statement.setDouble(5, z);
            statement.setString(6, world.getName());
            statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean removeWaypoint(String playerUUID, String waypointName) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM waypoints WHERE player_uuid=? AND waypoint_name=?");
            statement.setString(1, playerUUID);
            statement.setString(2, waypointName);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static List<String> getWaypoints(String playerUUID) {
        List<String> waypoints = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM waypoints WHERE player_uuid=?");
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                waypoints.add(result.getString("waypoint_name") + " -> " +
                        result.getString("world") +
                        " - X: " + result.getDouble("x") +
                        " - Y: " + result.getDouble("y") +
                        " - Z: " + result.getDouble("z"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return waypoints;
    }

    public static int getWaypointsCount(String playerUUID) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM waypoints WHERE player_uuid=?");
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean waypointExist(String playerUUID, String waypointName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM waypoints WHERE player_uuid=? AND waypoint_name=?");
            statement.setString(1, playerUUID);
            statement.setString(2, waypointName);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
