package fr.communaywen.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class VersionCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public VersionCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("version")) {
            sender.sendMessage("Version du plugin : " + plugin.getDescription().getVersion());
            return true;
        }
        return false;
    }
}
