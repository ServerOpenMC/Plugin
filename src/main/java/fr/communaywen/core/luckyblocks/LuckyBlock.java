package fr.communaywen.core.luckyblocks;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("plugin on");
        getCommand("lucky").setExecutor(new LuckyBlockCommands());
        getServer().getPluginManager().registerEvents(new LuckyBlockListeners(), this);
    }

    private void createConfig() {
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println("plugin off");
    }

}
