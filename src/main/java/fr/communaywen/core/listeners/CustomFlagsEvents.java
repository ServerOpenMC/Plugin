package fr.communaywen.core.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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

        if (!set.testState(null, (StateFlag) plugin.getCustomFlags().get(StateFlag.class).get("disable-fly"))) {
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
        }
    }
}
