package fr.communaywen.core.waypoints.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class WaypointManager {

    private final int maxWaypoints;

    public WaypointManager(FileConfiguration waypointConfig) {
        this.maxWaypoints = waypointConfig.getInt("maxWaypoints");
    }
}
