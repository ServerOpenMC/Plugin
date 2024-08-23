package fr.communaywen.core.dreamdim.advancements;

import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Nightmare implements Listener {
    AdvancementRegister register;

    public Nightmare(AdvancementRegister register) {
        /* Accordée quand le joueur meurt dans la dimension des rêves */
        this.register = register;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals("dreamworld")) { return; }
        register.grantAdvancement(p, "aywen:nightmare");
    }
}
