package fr.communaywen.core.homes.menu.utils;

import fr.communaywen.core.homes.Home;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.entity.Player;

public class HomeUtils {

    public static void changeHomeIcon(Player player, Home home) {
        home.setIcon(HomeMenuUtils.getHomeIcon(home));
        MessageManager.sendMessageType(player, "§aL'icône de la maison a été changée avec succès.", Prefix.HOME, MessageType.SUCCESS, true);
    }

}
