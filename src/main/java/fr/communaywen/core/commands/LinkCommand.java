package fr.communaywen.core.commands;

import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {

    private final LinkerAPI linkerAPI;

    public LinkCommand(LinkerAPI linkerAPI) { this.linkerAPI = linkerAPI; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { return false;}

        player.sendMessage("En cours de construction");

        return true;
    }
}
