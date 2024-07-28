package fr.communaywen.core.corporation.shops;

import fr.communaywen.core.AywenCraftPlugin;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.UUID;

@Getter
public class ShopItem {

    private final UUID itemID = UUID.randomUUID();
    private final ItemStack item;
    private final double pricePerItem;
    private double price;
    private int amount;

    public ShopItem(ItemStack item, double pricePerItem) {
        this.item = item.clone();
        this.pricePerItem = pricePerItem;
        this.item.setAmount(1);
        this.price = pricePerItem * amount;
        this.amount = 0;
    }

    public ShopItem setAmount(int amount) {
        this.amount = amount;
        this.price = pricePerItem * amount;
        return this;
    }

    public ShopItem copy() {
        return new ShopItem(item.clone(), pricePerItem);
    }

    public double getPrice(int amount) {
        return pricePerItem * amount;
    }

    public static String getItemName(Player localPlayer, ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                return itemMeta.getDisplayName();
            }
        }
        // If no custom name, return default name
        return getDefaultItemName(localPlayer, itemStack);
    }

    private static String getDefaultItemName(Player localPlayer, ItemStack itemStack) {
        Material material = itemStack.getType();
        String translationKey = material.getTranslationKey();

        TranslatableComponent translatable = Component.translatable(translationKey);

        Component translated = GlobalTranslator.render(translatable, Locale.of(localPlayer.getLocale()));

        return PlainTextComponentSerializer.plainText().serialize(translated);
    }
}
