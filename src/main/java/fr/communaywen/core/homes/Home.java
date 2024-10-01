package fr.communaywen.core.homes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;


public class Home {
    @Getter private final String player;
    @Getter @Setter private String name;
    private final Location location;

    public Home(String playerUUID, String name, Location location) {
        this.player = playerUUID;
        this.name = name;
        this.location = location;
    }

    public String serializeLocation() {
        return location.getWorld().getName() + "," +
                location.getBlockX() + "," +
                location.getBlockY() + "," +
                location.getBlockZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    public static Location deserializeLocation(String locString) {
        String[] loc = locString.split(",");
        return new Location(
                org.bukkit.Bukkit.getWorld(loc[0]),
                Double.parseDouble(loc[1]),
                Double.parseDouble(loc[2]),
                Double.parseDouble(loc[3]),
                Float.parseFloat(loc[4]),
                Float.parseFloat(loc[5])
        );
    }

    public Location getLocation() {
        return location;
    }
}
