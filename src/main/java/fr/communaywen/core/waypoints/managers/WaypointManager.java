package fr.communaywen.core.waypoints.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class WaypointManager {

    /**
     * The maximum amount of waypoints a player can have
     * (can be changed in the waypoints.yml config file)
     */
    private final int maxWaypoints;

    public WaypointManager(FileConfiguration waypointConfig) {
        this.maxWaypoints = waypointConfig.getInt("maxWaypoints");
    }
}
