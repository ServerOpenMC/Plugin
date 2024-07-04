package fr.communaywen.core;

import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);

        this.getCommand("version").setExecutor(new VersionCommand(this));
    }

    @Override
    public void onDisable() {
    }
}
