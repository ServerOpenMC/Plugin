package fr.communaywen.core.levels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;

public class LevelsListeners implements Listener {

    private LevelsManager levelsManager;

    public LevelsListeners(LevelsManager levelsManager) {
        this.levelsManager = levelsManager;
    }

    @EventHandler
    public void EntityDeathEvent( EntityDeathEvent event ) {
         Entity entity = event.getDamageSource().getCausingEntity();

         if ( entity != null ) {
             if (entity instanceof Player player) {
                 levelsManager.addExperience(10, player);
                 player.sendMessage("+ 10 xp !");
             }
         }

     }
}
