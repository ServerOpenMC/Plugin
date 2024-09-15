package fr.communaywen.core.luckyblocks.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class LBUtils {

    public static String getBlockNamespaceID() {
        return "luckyblock:luckyblock";
    }

    public static ItemStack getLuckyBlockItem() {
        CustomStack lb = CustomStack.getInstance(getBlockNamespaceID());

        return lb.getItemStack();
    }
}
