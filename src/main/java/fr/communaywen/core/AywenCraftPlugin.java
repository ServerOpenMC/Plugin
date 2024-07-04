package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info("Hello le monde, ici le plugin AywenCraft !");
        getServer().getPluginManager().registerEvents(new AntiTrampling(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
