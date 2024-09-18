package fr.communaywen.core.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
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
}
