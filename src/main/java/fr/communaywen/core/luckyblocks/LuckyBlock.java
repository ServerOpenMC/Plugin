package fr.communaywen.core.luckyblocks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new LuckyBlockEvents(), this);
        getLogger().info("plugin on");
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin off");
    }
}
