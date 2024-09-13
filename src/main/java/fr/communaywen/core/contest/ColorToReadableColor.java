package fr.communaywen.core.contest;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class ColorToReadableColor {

    private static final Map<ChatColor, ChatColor> colorToReadable = new HashMap<>();
    static {
        colorToReadable.put(ChatColor.BLACK, ChatColor.BLACK);
        colorToReadable.put(ChatColor.DARK_BLUE, ChatColor.DARK_BLUE);
        colorToReadable.put(ChatColor.DARK_GREEN, ChatColor.DARK_GREEN);
        colorToReadable.put(ChatColor.DARK_AQUA, ChatColor.DARK_AQUA);
        colorToReadable.put(ChatColor.DARK_RED, ChatColor.DARK_RED);
        colorToReadable.put(ChatColor.DARK_PURPLE, ChatColor.DARK_PURPLE);
        colorToReadable.put(ChatColor.GOLD, ChatColor.GOLD);
        colorToReadable.put(ChatColor.GRAY, ChatColor.GRAY);
        colorToReadable.put(ChatColor.DARK_GRAY, ChatColor.DARK_GRAY);
        colorToReadable.put(ChatColor.BLUE, ChatColor.BLUE);
        colorToReadable.put(ChatColor.GREEN, ChatColor.GREEN);
        colorToReadable.put(ChatColor.AQUA, ChatColor.AQUA);
        colorToReadable.put(ChatColor.RED, ChatColor.RED);
        colorToReadable.put(ChatColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE);
        colorToReadable.put(ChatColor.YELLOW, ChatColor.GOLD);
        colorToReadable.put(ChatColor.WHITE, ChatColor.GRAY);
    }


    public static ChatColor getReadableColor(ChatColor c) {
        return colorToReadable.getOrDefault(c, null);
    }

}
