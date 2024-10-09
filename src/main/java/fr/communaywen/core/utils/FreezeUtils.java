package fr.communaywen.core.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;

public class FreezeUtils {

    public static void switch_freeze(Player player, Player target) {
        if (target == null) {
            player.sendMessage(ChatColor.DARK_RED + "Joueur introuvable !");
        } else {
            if (!AywenCraftPlugin.frozenPlayers.contains(target)) {
                target.sendTitle(ChatColor.DARK_RED + "Vous êtes freeze !", ChatColor.YELLOW + "Regardez votre chat", 10, 70, 20);
                MessageManager.sendMessageType(target, ChatColor.DARK_RED + "Vous êtes freeze, si vous vous déconnectez, vous serez banni !", Prefix.FREEZE, MessageType.WARNING, false);
                MessageManager.sendMessageType(player, ChatColor.DARK_RED + "Vous avez freeze " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_RED + " !", Prefix.FREEZE, MessageType.INFO, false);
                AywenCraftPlugin.frozenPlayers.add(target);
            } else {
                target.resetTitle();
                MessageManager.sendMessageType(target, ChatColor.DARK_GREEN + "Tu as été unfreeze !", Prefix.FREEZE, MessageType.INFO, false);
                MessageManager.sendMessageType(player, ChatColor.DARK_GREEN + "Vous avez unfreeze " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_GREEN + " !", Prefix.FREEZE, MessageType.INFO, false);
                AywenCraftPlugin.frozenPlayers.remove(target);
            }
        }
    }
}
