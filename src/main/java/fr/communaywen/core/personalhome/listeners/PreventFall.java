package fr.communaywen.core.personalhome.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.personalhome.Home;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PreventFall implements Listener {
    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) { return; }
        if (!(e.getEntity() instanceof Player player)) { return; }

        Home home = AywenCraftPlugin.getInstance().getManagers().getHomeManager().getHomes().get(player.getUniqueId());

        player.teleport(home.getSpawnpoint());
    }
}
