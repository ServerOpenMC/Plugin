package fr.communaywen.core.utils;



import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.inventory.Inventory;

public class ItemUtils {
    public static List<ItemStack> splitAmountIntoStack(ItemStack items) {
        int amount = items.getAmount();

        List<ItemStack> stacks = new ArrayList<>();
        while (amount > 64) {
            ItemStack item = items.clone();
            item.setAmount(64);
            stacks.add(item);

            amount -= 64;
        }

        if (amount > 0) {
            ItemStack item = items.clone();
            item.setAmount(amount);
            stacks.add(item);
        }

        return stacks;
    }

    public static int getNumberItemToStack(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();
        int numberitemtostack = 0;

        for (ItemStack stack : inventory.getStorageContents()) {
           if (stack != null && stack.isSimilar(item)) {
               numberitemtostack = 64 - stack.getAmount();
            }
        }
        return numberitemtostack;
    }

    public static int getSlotNull(Player player) {
        Inventory inventory = player.getInventory();

        int slot = 0;

        for (ItemStack stack : inventory.getStorageContents()) {
            if (stack == null) {
                slot++;
            }
        }

        return slot;
    }


    // IMPORT FROM AXENO
    public static boolean hasEnoughItems(Player player, Material item, int amount) {
        int totalItems = 0;
        ItemStack[] contents = player.getInventory().getContents();

        for (ItemStack is : contents) {
            if (is != null && is.getType() == item) {
                totalItems += is.getAmount();
            }
        }

        if (amount == 0) return false;
        return totalItems >= amount;
    }

    public static void removeItemsFromInventory(Player player, Material item, int quantity) {
        ItemStack[] contents = player.getInventory().getContents();
        int remaining = quantity;

        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == item) {
                int stackAmount = stack.getAmount();
                if (stackAmount <= remaining) {
                    player.getInventory().setItem(i, null);
                    remaining -= stackAmount;
                } else {
                    stack.setAmount(stackAmount - remaining);
                    remaining = 0;
                }
            }
        }
    }
    public static Component getDefaultItemName(Material material) {
        return Component.translatable(material.translationKey());
    }

    public static Component getDefaultItemName(ItemStack itemStack) {
        return getDefaultItemName(itemStack.getType());
    }
}
