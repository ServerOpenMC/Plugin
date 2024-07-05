package fr.communaywen.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUtils {

    public static boolean sendMessage(CommandSender sender, String message, boolean error) {
        sender.sendMessage((error ? ChatColor.RED : ChatColor.GOLD) + message);
        return !error;
    }

}
