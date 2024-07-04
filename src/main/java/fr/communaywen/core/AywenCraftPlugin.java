package fr.communaywen.core;

import fr.communaywen.core.utils.MOTDChanger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        // Init + Launch MOTD
        motdChanger = new MOTDChanger();
        motdChanger.onEnable();
    }

    @Override
    public void onDisable() {
        // Disable MOTD
        if (motdChanger != null) {
            motdChanger.onDisable();
        }
    }
}
