package fr.communaywen.core;

import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.commands.TeamCommand;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;
    private TeamManager teamManager;
    private static AywenCraftPlugin instance;

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        instance = this;

        MenuLib.init(this);

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);
        teamManager = new TeamManager();

        this.getCommand("version").setExecutor(new VersionCommand(this));
        PluginCommand teamCommand = this.getCommand("team");
        teamCommand.setExecutor(new TeamCommand());
        teamCommand.setTabCompleter(new TeamCommand());
    }

    @Override
    public void onDisable() {
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static AywenCraftPlugin getInstance() {
        return instance;
    }
}
