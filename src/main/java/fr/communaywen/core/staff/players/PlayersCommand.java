package fr.communaywen.core.staff.players;

import fr.communaywen.core.utils.PlayersMenuUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class PlayersCommand {
	@Command("players")
	@Description("Ouvre le menu des joueurs")
	@CommandPermission("openmc.staff.players")
	public void onCommand(Player player) {
		player.sendMessage(ChatColor.DARK_RED + "La commande /players est temporairement indisponible car incompatible avec EssentialsX");
		 // PlayersMenuUtils.openPlayersMenu(player);
	}
}