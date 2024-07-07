package fr.communaywen.core.staff.players;

import fr.communaywen.core.utils.PlayersMenuUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayersCommand implements CommandExecutor {
	 @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 if (sender instanceof Player player) {
			if (args.length != 0) {
				player.sendMessage(ChatColor.RED + "La commande est " + ChatColor.BLUE + "/players");
				return false;
			}
			PlayersMenuUtils.openPlayersMenu(player);
			return true;
		 } else {
			 return false;
		 }
	}
}