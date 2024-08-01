package fr.communaywen.core.customitems.objects;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomItems {

    ItemStack getItemStack();
    default void onBlockBreak(BlockBreakEvent event) {}
    default void onEnchant(EnchantItemEvent event) {}
    default void onAnvil(PrepareAnvilEvent event) {}
}
