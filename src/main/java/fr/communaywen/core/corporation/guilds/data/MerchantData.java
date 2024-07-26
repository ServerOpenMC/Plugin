package fr.communaywen.core.corporation.guilds.data;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MerchantData {

    private final List<ItemStack> depositedItems = new ArrayList<>();
    private double moneyWon = 0;

    public int getAllDepositedItemsAmount() {
        int amount = 0;
        for (ItemStack item : depositedItems) {
            amount += item.getAmount();
        }
        return amount;
    }

    public void depositItem(ItemStack item) {
        depositedItems.add(item);
    }

    public void addMoneyWon(double money) {
        moneyWon += money;
    }

}
