package fr.communaywen.core.commands;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.*;

@Command("englishorspanish")
@Description("English or Spanish?")
@CommandPermission("ayw.command.englishorspanish")
public class EnglishOrSpanishCommand {
    public static final List<Player> players = new ArrayList<>();

    @Subcommand("english")
    public void sendEnglish(Player player) {
        players.add(player);
        player.sendMessage("Whoever moves first is gay");
    }

    @Subcommand("spanish")
    public void sendSpanish(Player player) {
        players.add(player);
        player.sendMessage("El primero que se mueva es gay");
    }
}
