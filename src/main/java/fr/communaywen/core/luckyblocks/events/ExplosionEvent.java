package fr.communaywen.core.luckyblocks.events;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public class ExplosionEvent implements Listener {

    public void trigger(Location location) {
        if (location != null && location.getWorld() != null) {
            location.getWorld().createExplosion(location, 2, true);
        }
    }
}
