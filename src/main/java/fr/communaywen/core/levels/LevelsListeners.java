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
         Entity entityDamaging = event.getDamageSource().getCausingEntity();
         Entity entityDamaged = event.getEntity();


         if ( entityDamaging != null ) {
             if (entityDamaging instanceof Player player) {
                levelsManager.applyExperienceReward(player,entityDamaged.getName().toLowerCase().replace(" ", "_"));
             }
         }

     }
}
