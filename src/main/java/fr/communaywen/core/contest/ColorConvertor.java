package fr.communaywen.core.contest;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ColorConvertor {

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

    private static final Map<ChatColor, Material> colorToMaterial = new HashMap<>();
    static {
        colorToMaterial.put(ChatColor.BLACK, Material.BLACK_WOOL);
        colorToMaterial.put(ChatColor.DARK_BLUE, Material.BLUE_WOOL);
        colorToMaterial.put(ChatColor.DARK_GREEN, Material.GREEN_WOOL);
        colorToMaterial.put(ChatColor.DARK_AQUA, Material.CYAN_WOOL);
        colorToMaterial.put(ChatColor.DARK_RED, Material.RED_WOOL);
        colorToMaterial.put(ChatColor.DARK_PURPLE, Material.PURPLE_WOOL);
        colorToMaterial.put(ChatColor.GOLD, Material.ORANGE_WOOL);
        colorToMaterial.put(ChatColor.GRAY, Material.LIGHT_GRAY_WOOL);
        colorToMaterial.put(ChatColor.DARK_GRAY, Material.GRAY_WOOL);
        colorToMaterial.put(ChatColor.BLUE, Material.LIGHT_BLUE_WOOL);
        colorToMaterial.put(ChatColor.GREEN, Material.LIME_WOOL);
        colorToMaterial.put(ChatColor.AQUA, Material.CYAN_WOOL);
        colorToMaterial.put(ChatColor.RED, Material.RED_WOOL);
        colorToMaterial.put(ChatColor.LIGHT_PURPLE, Material.MAGENTA_WOOL);
        colorToMaterial.put(ChatColor.YELLOW, Material.YELLOW_WOOL);
        colorToMaterial.put(ChatColor.WHITE, Material.WHITE_WOOL);
    }

    public static Material getMaterialFromColor(ChatColor c) {
        return colorToMaterial.getOrDefault(c, null);
    }

    private static final Map<ChatColor, Color> firerocketColorFromChatColor = new HashMap<>();
    static {
        firerocketColorFromChatColor.put(ChatColor.BLACK, Color.BLACK);
        firerocketColorFromChatColor.put(ChatColor.DARK_BLUE, Color.BLUE);
        firerocketColorFromChatColor.put(ChatColor.DARK_GREEN, Color.GREEN);
        firerocketColorFromChatColor.put(ChatColor.DARK_AQUA, Color.AQUA);
        firerocketColorFromChatColor.put(ChatColor.DARK_RED, Color.RED);
        firerocketColorFromChatColor.put(ChatColor.DARK_PURPLE, Color.PURPLE);
        firerocketColorFromChatColor.put(ChatColor.GOLD, Color.ORANGE);
        firerocketColorFromChatColor.put(ChatColor.GRAY, Color.SILVER);
        firerocketColorFromChatColor.put(ChatColor.DARK_GRAY, Color.GRAY);
        firerocketColorFromChatColor.put(ChatColor.BLUE, Color.AQUA);
        firerocketColorFromChatColor.put(ChatColor.GREEN, Color.LIME);
        firerocketColorFromChatColor.put(ChatColor.AQUA, Color.AQUA);
        firerocketColorFromChatColor.put(ChatColor.RED, Color.RED);
        firerocketColorFromChatColor.put(ChatColor.LIGHT_PURPLE, Color.FUCHSIA);
        firerocketColorFromChatColor.put(ChatColor.YELLOW, Color.YELLOW);
        firerocketColorFromChatColor.put(ChatColor.WHITE, Color.WHITE);
    }

    public static Color getFirerocketColorFromChatColor(ChatColor c) {
        return firerocketColorFromChatColor.getOrDefault(c, null);
    }

}
