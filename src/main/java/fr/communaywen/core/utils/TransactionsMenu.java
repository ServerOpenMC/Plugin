package fr.communaywen.core.utils;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import fr.communaywen.core.utils.database.TransactionsManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionsMenu extends Menu {
    Player owner;
    UUID target;

    public TransactionsMenu(Player owner, UUID target) {
        super(owner);
        this.owner = owner;
        this.target = target;
    }

    @Override
    public @NotNull String getName() {
        return "Transactions de "+ Bukkit.getOfflinePlayer(target).getName();
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> items = new HashMap<>();
        int iter = 0;
        for (Transaction transaction: new TransactionsManager().getTransactionsByPlayers(target, 54)) {
            items.put(iter, transaction.toItemStack(target));
            iter++;
        }

        return items;
    }
}