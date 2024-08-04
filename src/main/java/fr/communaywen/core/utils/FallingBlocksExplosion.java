package fr.communaywen.core.utils;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;

import java.util.Objects;

public class FallingBlocksExplosion {

    public FallingBlocksExplosion(float radius, Location location, boolean fire) {
        Objects.requireNonNull(location, "Location cannot be null");
        Objects.requireNonNull(location.getWorld(), "Location world cannot be null");

        AywenCraftPlugin.getInstance().getManagers().getFbeManager().addLocation(location);
        location.getWorld().createExplosion(location, radius, fire);


    }
}
