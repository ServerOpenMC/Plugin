package fr.communaywen.core.commands;

import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ManualLinkCommand implements CommandExecutor {

    private final LinkerAPI linkerAPI;

    public ManualLinkCommand(LinkerAPI linkerAPI) { this.linkerAPI = linkerAPI; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        if (args.length != 1) {
            sender.sendMessage("Un userId est requis en argument");
            return true;
        }
        Player player = (Player) sender;

        try {
            this.linkerAPI.setDatabase(player, args[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
