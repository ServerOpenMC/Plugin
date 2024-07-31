package fr.communaywen.core.waypoints;

import lombok.Getter;

@Getter
public class Waypoint {

    private final String name;
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    public Waypoint(String name, String world, double x, double y, double z) {
        this.name = name;
        this.worldName = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
