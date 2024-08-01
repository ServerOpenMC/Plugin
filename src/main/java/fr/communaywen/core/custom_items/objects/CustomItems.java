package fr.communaywen.core.custom_items.objects;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomItems {

    ItemStack getItemStack();
    default void onBlockBreak(BlockBreakEvent event) {}
}
