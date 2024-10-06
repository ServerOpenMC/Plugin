package fr.communaywen.core.adminshop.menu.category.colored;

import fr.communaywen.core.adminshop.shopinterfaces.ColoredShop;
import org.bukkit.entity.Player;

public class AdminColoredShop extends ColoredShop {
    public AdminColoredShop(Player player, BlockType blockType) {
        super(player, "§6Admin Shop - §eBlocks", getName(blockType), blockType.getName());
    }

    private static String getName(BlockType blockType) {
        return switch (blockType) {
            case GLASS -> "stained_glass";
            case GLASS_PANE -> "stained_glass_pane";
            default -> blockType.name().toLowerCase();
        };
    }
}
