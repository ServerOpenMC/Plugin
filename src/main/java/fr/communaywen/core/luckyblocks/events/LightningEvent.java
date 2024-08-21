package fr.communaywen.core.luckyblocks.events;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public class LightningEvent implements Listener {

    public void trigger(Location location) {
        if (location != null && location.getWorld() != null) {
            location.getWorld().strikeLightningEffect(location);
        }
    }
}

