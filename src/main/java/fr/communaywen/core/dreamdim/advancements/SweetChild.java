package fr.communaywen.core.dreamdim.advancements;

import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class SweetChild implements Listener {
    AdvancementRegister register;

    public SweetChild(AdvancementRegister register) {
        /* Accordée quand le joueur entre dans la dimension des rêves */
        this.register = register;
    }

    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("dreamworld")) {
            register.grantAdvancement(e.getPlayer(), "aywen:root");
        }
    }
}
