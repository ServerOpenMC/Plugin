package fr.communaywen.core.listeners;

import fr.communaywen.core.commands.ExplodeRandomCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionListener implements Listener {
    @EventHandler
    public void onPrimedTNTExplosion(EntityExplodeEvent event) {
        if (ExplodeRandomCommand.preventedExplosvies.contains(event.getEntity())) {
            event.blockList().clear();
            ExplodeRandomCommand.preventedExplosvies.remove(event.getEntity());
        }
    }
}
