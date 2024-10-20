package fr.communaywen.core.adminshop.menu.buy;

import com.google.common.util.concurrent.AtomicDouble;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminShopBuy extends Menu {
    BaseItems items;
    private final AtomicInteger number;
    private final AtomicDouble prize;
    private static final int MAX_ITEMS = 255;
    private String material = null;

    public AdminShopBuy(Player player, BaseItems items) {
        super(player);
        this.items = items;
        this.number = new AtomicInteger(1);
        this.prize = new AtomicDouble(items.getPrize());
    }

    public AdminShopBuy(Player player, BaseItems items, String material) {
        super(player);
        this.items = items;
        this.number = new AtomicInteger(1);
        this.prize = new AtomicDouble(items.getPrize());
        this.material = material;
    }

    @Override
    public @NotNull String getName() {
        return "§6Admin Shop - §eConfirmation achat";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            content.put(i, new ItemBuilder(this, Material.BLACK_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        content.put(10, createChangeButton(-64, Material.PURPLE_STAINED_GLASS_PANE, "§5Retirer 64"));
        content.put(11, createChangeButton(-10, Material.RED_STAINED_GLASS_PANE, "§cRetirer 10"));
        content.put(12, createChangeButton(-1, Material.RED_STAINED_GLASS_PANE, "§cRetirer 1"));

        content.put(13, createItemDisplay());

        content.put(14, createChangeButton(1, Material.GREEN_STAINED_GLASS_PANE, "§aAjouter 1"));
        content.put(15, createChangeButton(10, Material.GREEN_STAINED_GLASS_PANE, "§aAjouter 10"));
        content.put(16, createChangeButton(64, Material.PURPLE_STAINED_GLASS_PANE, "§5Ajouter 64"));

        return content;
    }

    private ItemStack createChangeButton(int amount, Material materials, String displayName) {
        return new ItemBuilder(this, materials, itemMeta -> {
            itemMeta.setDisplayName(displayName);
        }).setOnClick(event -> {
            int newNumber = number.get() + amount;
            if (newNumber >= 1 && newNumber <= MAX_ITEMS) {
                number.set(newNumber);
                updateDisplay(event.getInventory());
            } else if (newNumber > MAX_ITEMS) {
                number.set(MAX_ITEMS);
                updateDisplay(event.getInventory());
                getOwner().sendMessage("§cVous ne pouvez pas acheter plus de " + MAX_ITEMS + " items à la fois.");
            }
        });
    }

    private ItemStack createItemDisplay() {
        return new ItemBuilder(this, Objects.requireNonNull(Material.getMaterial(material == null ? items.named() : items.named() + "_" + material)), itemMeta -> {
            itemMeta.setDisplayName(items.getName());
            updateItemMeta(itemMeta);
        }).setNextMenu(new AdminShopBuyConfirm(getOwner(), items, number.get(), material));
    }

    private void updateItemMeta(ItemMeta itemMeta) {
        double finalPrize = prize.get() * number.get();
        itemMeta.setLore(Arrays.asList(
                "  §8■ §7Quantité: §e" + number.get(),
                "  §8■ §7Prix final: §e" + String.format("%.2f", finalPrize) + "$",
                "",
                "§eCliquez pour confirmer l'achat !"
        ));
    }

    private void updateDisplay(Inventory inventory) {
        ItemStack itemStack = inventory.getItem(13);
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                updateItemMeta(itemMeta);
                itemStack.setItemMeta(itemMeta);
            }

            inventory.setItem(13, createItemDisplay());
        }
        getOwner().updateInventory();
    }
}
