package fr.communaywen.core.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomFlagsEvents implements Listener {

    static AywenCraftPlugin plugin;
    public CustomFlagsEvents(AywenCraftPlugin plugins) {
        plugin = plugins;

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFlyToggle(org.bukkit.event.player.PlayerToggleFlightEvent event) {
        // WorldGuard
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(event.getPlayer().getLocation()));

        if(event.getPlayer().hasPermission("essentials.fly")) {
            event.setCancelled(false);
            return;
        }

        if (!set.testState(null, (StateFlag) plugin.getCustomFlags().get(StateFlag.class).get("disable-fly"))) {
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cVous n'êtes pas autorisé à faire cela ici !");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // WorldGuard
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        if (event.getMaterial() == Material.WIND_CHARGE) {
            if (!set.testState(null, (StateFlag) plugin.getCustomFlags().get(StateFlag.class).get("disable-wind-charge"))) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
                player.sendMessage("§cVous n'êtes pas autorisé à faire cela ici !");
            }
        }
    }
}
