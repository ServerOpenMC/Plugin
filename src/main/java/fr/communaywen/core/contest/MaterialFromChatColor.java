package fr.communaywen.core.contest;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialFromChatColor {
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

}
