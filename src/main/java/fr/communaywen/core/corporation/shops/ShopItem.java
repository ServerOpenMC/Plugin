package fr.communaywen.core.corporation.shops;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class ShopItem {

    private final UUID supplier;
    private final ItemStack item;
    private final double pricePerItem;
    private double price;
    private int amount;

    public ShopItem(UUID supplier, ItemStack item, double pricePerItem, int amount) {
        this.supplier = supplier;
        this.item = item;
        this.pricePerItem = pricePerItem;
        item.setAmount(Math.min(amount, 64));
        this.price = pricePerItem * amount;
        this.amount = amount;
    }

    public ShopItem setAmount(int amount) {
        this.amount = amount;
        item.setAmount(Math.min(amount, 64));
        this.price = pricePerItem * amount;
        return this;
    }

    public ShopItem copy() {
        return new ShopItem(supplier, item.clone(), pricePerItem, amount);
    }

    public double getPrice(int amount) {
        return pricePerItem * amount;
    }
}
