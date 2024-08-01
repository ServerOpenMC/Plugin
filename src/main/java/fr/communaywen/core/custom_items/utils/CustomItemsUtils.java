package fr.communaywen.core.custom_items.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CustomItemsUtils {

    public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackMeta = itemStack.getItemMeta();
        itemStackMeta.setDisplayName(name);
        itemStackMeta.setLore(lore);
        itemStack.setItemMeta(itemStackMeta);

        return itemStack;
    }
}
