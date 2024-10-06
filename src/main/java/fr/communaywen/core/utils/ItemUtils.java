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

    public static String getDefaultItemName(Player localPlayer, ItemStack itemStack) {
        Material material = itemStack.getType();
        String translationKey = material.getTranslationKey();

        TranslatableComponent translatable = Component.translatable(translationKey);

        Component translated = GlobalTranslator.render(translatable, Locale.of(localPlayer.getLocale()));

        return PlainTextComponentSerializer.plainText().serialize(translated);
    }
}
