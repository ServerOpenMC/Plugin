package fr.communaywen.core.adminshop.menu.category.colored;

import fr.communaywen.core.adminshop.shopinterfaces.ColoredShop;
import org.bukkit.entity.Player;

public class AdminColoredShop extends ColoredShop {
    public AdminColoredShop(Player player, BlockType blockType) {
        super(player, "§6Admin Shop - §eBlocks", blockType.name(), blockType.getName());
    }
}
