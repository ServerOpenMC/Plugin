package fr.communaywen.core.adminshop.menu.buy;

import com.google.common.util.concurrent.AtomicDouble;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import org.bukkit.Bukkit;
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
    private final BaseItems items;
    private final AtomicInteger number;
    private final AtomicDouble prize;
    private static final int MAX_ITEMS = 255;
    private final String material;

    public AdminShopBuy(Player player, BaseItems items) {
        this(player, items, null);
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
        return InventorySize.NORMAL; // Assuming NORMAL returns 27 slots (3 rows)
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        // To be implemented: Handle click events here
        event.setCancelled(true); // Cancel the event to prevent players from taking items
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();
        int inventorySize = getInventorySize().getSize();

        for (int i = 0; i < inventorySize; i++) {
            content.put(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .setDisplayName(" ")
                    .build());
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

    private ItemStack createChangeButton(int amount, Material material, String displayName) {
        return new ItemBuilder(material)
                .setDisplayName(displayName)
                .setOnClick(event -> {
                    int newNumber = number.get() + amount;
                    if (newNumber >= 1 && newNumber <= MAX_ITEMS) {
                        number.set(newNumber);
                        updateDisplay(event.getInventory());
                    } else if (newNumber > MAX_ITEMS) {
                        number.set(MAX_ITEMS);
                        updateDisplay(event.getInventory());
                        getOwner().sendMessage("§cVous ne pouvez pas acheter plus de " + MAX_ITEMS + " items à la fois.");
                    }
                })
                .build();
    }

    private ItemStack createItemDisplay() {
        Material itemMaterial = Material.getMaterial(material == null ? items.named() : items.named() + "_" + material);
        Objects.requireNonNull(itemMaterial, "Material cannot be null");

        return new ItemBuilder(itemMaterial)
                .setDisplayName(items.getName())
                .setLore(getItemLore())
                .setOnClick(event -> new AdminShopBuyConfirm(getOwner(), items, number.get(), material).open())
                .build();
    }

    private List<String> getItemLore() {
        double finalPrize = prize.get() * number.get();
        return Arrays.asList(
                "  §8■ §7Quantité: §e" + number.get(),
                "  §8■ §7Prix final: §e" + String.format("%.2f", finalPrize) + "$",
                "",
                "§eCliquez pour confirmer l'achat !"
        );
    }

    private void updateDisplay(Inventory inventory) {
        ItemStack itemStack = inventory.getItem(13);
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setLore(getItemLore());
                itemStack.setItemMeta(itemMeta);
            }
        }
        getOwner().updateInventory();
    }
}
