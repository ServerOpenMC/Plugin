package fr.communaywen.core.settings;

import org.bukkit.ChatColor;

public class SettingsUtils {
	
	public static String getMailStatus(int mailAccept) {
		return switch (mailAccept) {
			case 0 -> ChatColor.RED + "Personne";
			case 1 -> ChatColor.GOLD + "Amis seulement";
			case 2 -> ChatColor.YELLOW + "Team seulement";
			default -> ChatColor.GREEN + "Tout le monde";
		};
	}
	
}
