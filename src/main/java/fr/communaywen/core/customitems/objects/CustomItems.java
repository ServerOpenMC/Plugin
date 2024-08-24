package fr.communaywen.core.customitems.objects;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomItems {

    void setName(String name);
    String getName();
    void setItemStack(ItemStack itemStack);
    ItemStack getItemStack();
    ArrayList<String> getRecipe();
    HashMap<Character, ItemStack> getIngredients();
    String getNamespacedID();
    default void onBlockBreak(BlockBreakEvent event) {}
    default void onEnchant(EnchantItemEvent event) {}
    default void onAnvil(PrepareAnvilEvent event) {}
}
