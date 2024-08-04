package fr.communaywen.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUtils {

    private static CommandSender sender;

    public static void setSender(CommandSender sender) {
        CommandUtils.sender = sender;
    }

    public static boolean sendMessage(CommandSender sender, String message, boolean error) {
        sender.sendMessage((error ? ChatColor.RED : ChatColor.GOLD) + message);
        return !error;
    }

    public static boolean sendMessage(String message, boolean error) {
        if (sender == null) {
            return false;
        }
        return sendMessage(sender, message, error);
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        return sendMessage(sender, message, false);
    }

    public static boolean sendMessage(String message) {
        return sendMessage(message, false);
    }

    public static boolean sendMessage(ChatColor color, String message, boolean isBold) {
        if (sender == null) {
            return false;
        }
        sender.sendMessage((isBold ? ChatColor.BOLD : "") + "" + color + message);
        return true;
    }

}
