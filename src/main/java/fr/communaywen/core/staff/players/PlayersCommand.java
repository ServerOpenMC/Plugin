package fr.communaywen.core.staff.players;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class PlayersCommand {
    @Command("players")
    @Description("Ouvre le menu des joueurs")
    public void onCommand(Player player) {
        PlayersMenu menu = new PlayersMenu(player);
        menu.open();
    }
}