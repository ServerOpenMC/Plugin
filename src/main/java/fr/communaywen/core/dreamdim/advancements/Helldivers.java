package fr.communaywen.core.dreamdim.advancements;

import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Helldivers implements Listener {
    AdvancementRegister register;

    public Helldivers(AdvancementRegister register) {
        /* Accordée quand le joueur tue un mob de chaque type dans la dimension de rêves */
        this.register = register;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (!(entity.getKiller() instanceof Player player)) { return; }
        if (!player.getWorld().getName().equals("dreamworld")) { return; }
        if (!entity.getWorld().getName().equals("dreamworld")) { return; }

        if (entity.getType().equals(EntityType.SPIDER)) {
            register.grantAdvancement(player, "aywen:helldivers/hd1", "spider");
        } else if (entity.getType().equals(EntityType.ZOMBIE_HORSE)) {
            register.grantAdvancement(player, "aywen:helldivers/hd1", "zombie_horse");
        } else if (entity.getType().equals(EntityType.SKELETON_HORSE)) {
            register.grantAdvancement(player, "aywen:helldivers/hd1", "skeleton_horse");
        }
    }
}
