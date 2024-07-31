package fr.communaywen.core.utils.database;

import fr.communaywen.core.waypoints.Waypoint;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Waypoints extends DatabaseConnector {

    /**
     * Add a waypoint to the database
     * @param playerUUID UUID of the player
     * @param waypointName Name of the waypoint
     * @param location Location of the waypoint
     * @return true if the waypoint has been added, false otherwise
     */
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

    /**
     * Remove a waypoint from the database by its name
     * @param playerUUID UUID of the player
     * @param waypointName Name of the waypoint
     * @return true if the waypoint has been removed, false otherwise
     */
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

    /**
     * Get all waypoints of a player
     * @param playerUUID UUID of the player
     * @return List of waypoints
     */
    public static List<Waypoint> getWaypoints(String playerUUID) {
        List<Waypoint> waypoints = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM waypoints WHERE player_uuid=?");
            statement.setString(1, playerUUID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                waypoints.add(new Waypoint(
                        result.getString("waypoint_name"),
                        result.getString("world"),
                        result.getDouble("x"),
                        result.getDouble("y"),
                        result.getDouble("z")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return waypoints;
    }

    /**
     * Get the number of waypoints of a player
     * @param playerUUID UUID of the player
     * @return Number of waypoints
     */
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

    /**
     * Check if a waypoint exists
     * @param playerUUID UUID of the player
     * @param waypointName Name of the waypoint
     * @return true if the waypoint exists, false otherwise
     */
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

    /**
     * Get a waypoint by its name
     * @param playerUUID UUID of the player
     * @param waypointName Name of the waypoint
     * @return Waypoint object
     */
    public static Waypoint getWaypoint(String playerUUID, String waypointName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM waypoints WHERE player_uuid=? AND waypoint_name=?");
            statement.setString(1, playerUUID);
            statement.setString(2, waypointName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Waypoint(
                        result.getString("waypoint_name"),
                        result.getString("world"),
                        result.getDouble("x"),
                        result.getDouble("y"),
                        result.getDouble("z")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
