package fr.communaywen.core.commands.staff;

import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.staff.players.PlayersMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Credit("gab400")
@Collaborators({"ddemile", "Martinouxx"})

public class PlayersCommand {
    @Command("players")
    @Description("Ouvre le menu des joueurs")
    @CommandPermission("openmc.staff.players")
    public void onCommand(Player player) {
        PlayersMenu menu = new PlayersMenu(player);
        menu.open();
    }
}