package fr.communaywen.core.waypoints.commands;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class WaypointManager {

    private FileConfiguration waypointConfig;
    @Getter
    private int maxWaypoints;

    public WaypointManager(FileConfiguration waypointConfig) {
        this.waypointConfig = waypointConfig;
        this.maxWaypoints = waypointConfig.getInt("maxWaypoints");
    }
}
