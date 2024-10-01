package fr.communaywen.core.spawn.head;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadListener implements Listener {
    static JavaPlugin plugin;

    public HeadListener(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

}
