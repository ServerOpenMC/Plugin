package fr.communaywen.core.utils.constant;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


@Feature("beautiful-message")
@Credit("Axeno")
public class MessageManager {


    /*

    For use the beautiful message, create a prefix.

     */


    /**
     * Sends a formatted message to the player with or without sound.
     *
     * @param player  The player to send the message to
     * @param message The content of the message
     * @param prefix  The prefix for the message
     * @param type    The type of message (information, error, success, warning)
     * @param sound   Indicates whether a sound should be played (true) or not (false)
     */
    public static void sendMessageType(Player player, String message, Prefix prefix, MessageType type, boolean sound) {

        if(sound) {
            player.playSound(player.getLocation(), getSound(type), 1, 1);
        }
        String messageStr = "§8(" + getPrefixType(type) + "§8) " + prefix.getPrefix() + " §7» " + message;

        player.sendMessage(messageStr);

    }


    /**
     * Sends a formatted message to the player with an accompanying sound.
     *
     * @param player  The player to send the message to
     * @param message The content of the message
     * @param prefix  The prefix for the message
     * @param type    The type of message (determines the sound played)
     */
    public static void sendMessage(Player player, String message, Prefix prefix, MessageType type) {
        player.playSound(player.getLocation(), getSound(type), 1, 1);

        String messageStr = prefix.getPrefix() + " §7» " + message;

        player.sendMessage(messageStr);

    }


    private static String getPrefixType(MessageType type) {
        String prefixType;
        switch (type) {
            case ERROR:
                prefixType = "§c❗";
                break;
            case WARNING:
                prefixType = "§6⚠";
                break;
            case SUCCESS:
                prefixType = "§a✔";
                break;
            case INFO:
                prefixType = "§bⓘ";
                break;
            default:
                prefixType = "§7";
                break;
        }
        return prefixType;
    }

    private static Sound getSound(MessageType type) {
        Sound soundType;
        switch (type) {
            case ERROR:
                soundType = Sound.BLOCK_NOTE_BLOCK_BASS;
                break;
            case WARNING:
                soundType = Sound.BLOCK_NOTE_BLOCK_BASS;
                break;
            case SUCCESS:
                soundType = Sound.BLOCK_NOTE_BLOCK_BELL;
                break;
            case INFO:
                soundType = Sound.BLOCK_NOTE_BLOCK_BIT;
                break;
            default:
                soundType = null;
                break;
        }
        return soundType;
    }

}
