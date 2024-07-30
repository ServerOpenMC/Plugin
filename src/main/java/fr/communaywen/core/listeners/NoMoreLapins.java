package fr.communaywen.core.listeners;

import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

// Dandan demanda
// Gyro fit
public class NoMoreLapins implements Listener {

    @EventHandler
    public void EntitiesLoad(EntitiesLoadEvent event) {
        event.getEntities().forEach(entity -> {
            if (entity instanceof Rabbit rabbit) {
                rabbit.setAI(false);
            }
        });
    }

    @EventHandler
    public void EntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Rabbit rabbit) {
            rabbit.setAI(false);
        }
    }
}
