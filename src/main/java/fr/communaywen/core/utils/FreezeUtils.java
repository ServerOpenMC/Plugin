package fr.communaywen.core.utils;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FreezeUtils {
	public static String prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "Freeze" + ChatColor.GRAY + "] ";
	
	public static void switch_freeze(Player player, Player target) {
		if (target == null) {
			player.sendMessage(prefix + ChatColor.DARK_RED + "Joueur introuvable !");
		} else {
			if (!AywenCraftPlugin.frozenPlayers.contains(target)) {
				target.sendTitle(ChatColor.DARK_RED + "Vous êtes freeze !", ChatColor.YELLOW + "Regardez votre chat", 10, 70, 20);
				target.sendMessage(prefix + ChatColor.DARK_RED + "Vous êtes freeze, si vous vous déconnectez, vous serez banni !");
				player.sendMessage(prefix + ChatColor.DARK_RED + "Vous avez freeze " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_RED + " !");
				AywenCraftPlugin.frozenPlayers.add(target);
				PlayersMenuUtils.state = "§4Freeze";
			} else {
				target.resetTitle();
				target.sendMessage(prefix + ChatColor.DARK_GREEN + "Tu as été unfreeze !");
				player.sendMessage(prefix + ChatColor.DARK_GREEN + "Vous avez unfreeze " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_GREEN + " !");
				AywenCraftPlugin.frozenPlayers.remove(target);
				PlayersMenuUtils.state = "§2Unfreeze";
			}
		}
	}
}
