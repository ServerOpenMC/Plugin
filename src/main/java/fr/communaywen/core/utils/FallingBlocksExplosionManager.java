package fr.communaywen.core.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class FallingBlocksExplosionManager {
    private final List<Location> locations = new ArrayList<>();


    public void addLocation(Location location) {
        this.locations.add(location);
    }

    public void removeLocation(Location location) {
        this.locations.remove(location);
    }

    public List<Location> getLocations() {
        return locations;
    }
}
