package fr.communaywen.core.waypoints.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class WaypointManager {

    @Getter
    private final int maxWaypoints;

    public WaypointManager(FileConfiguration waypointConfig) {
        this.maxWaypoints = waypointConfig.getInt("maxWaypoints");
    }
}
