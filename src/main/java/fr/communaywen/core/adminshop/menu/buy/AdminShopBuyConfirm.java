package fr.communaywen.core.adminshop.menu.buy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.Transaction;
import fr.communaywen.core.utils.database.TransactionsManager;

public class AdminShopBuyConfirm extends Menu {
    private final BaseItems items;
    private final int quantity;
    private final String material;

    TransactionsManager transactionsManager = new TransactionsManager();

    public AdminShopBuyConfirm(Player player, BaseItems items, int quantity, String material) {
        super(player);
        this.items = items;
        this.quantity = quantity;
        this.material = material;
    }

    @Override
    public @NotNull String getName() {
        return "§6Confirmer l'achat";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
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

        content.put(2, new ItemBuilder(this, Material.GREEN_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName("§aConfirmer");
        }).setOnClick(event -> {
            if (!hasEnoughSpace(getOwner(), Material.getMaterial(material == null ? items.named() : items.named() + "_" + material), quantity)) {
                getOwner().sendMessage(ChatColor.RED + "Vous n'avez pas assez d'espace dans votre inventaire !");
                getOwner().closeInventory();
                return;
            } else {
                EconomyManager economy = AywenCraftPlugin.getInstance().getManagers().getEconomyManager();
                double balance = economy.getBalance(getOwner().getUniqueId());
                if(balance < (items.getPrize() * quantity)) {
                    getOwner().sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour acheter cette item.");
                } else {
                    int maxStackSize = items.getMaxStack();
                    int totalQuantity = quantity;
                    Material materials = Material.getMaterial((material == null) ? items.named() : (items.named() + "_" + material));

                    economy.withdrawBalance(getOwner().getUniqueId(), (items.getPrize() * totalQuantity));

                    transactionsManager.addTransaction(new Transaction(
                            "CONSOLE",
                            getOwner().getUniqueId().toString(),
                            (items.getPrize() * totalQuantity),
                            "Achat adminshop"
                    ));

                    getOwner().sendMessage("§aAchat confirmé !");
                    getOwner().sendMessage("  §2+ §a" + totalQuantity + " " + items.getName() + " §7pour §a" + String.format("%.2f", items.getPrize() * totalQuantity) + "$");

                    while (totalQuantity > 0) {
                        int stackSize = Math.min(totalQuantity, maxStackSize);
                        getOwner().getInventory().addItem(new ItemStack(materials, stackSize));
                        totalQuantity -= stackSize;
                    }

                }
            }
            getOwner().closeInventory();
        }));

        content.put(4, new ItemBuilder(this, Objects.requireNonNull(Material.getMaterial(material == null ? items.named() : items.named() + "_" + material)), itemMeta -> {
            itemMeta.setDisplayName(items.getName());
            double finalPrize = items.getPrize() * quantity;
            itemMeta.setLore(Arrays.asList(
                    "§7Quantité: §e" + quantity,
                    "§7Prix total: §e" + String.format("%.2f", finalPrize) + "$"
            ));
        }));

        content.put(6, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName("§cAnnuler");
        }).setBackButton());

        return content;
    }

    private boolean hasEnoughSpace(Player player, Material item, int amount) {
        ItemStack[] contents = player.getInventory().getContents();

        for(ItemStack is : contents) {
            if(is == null || is.getType() == Material.AIR) {
                continue;
            }
            if(is.getType() == item && is.getAmount() < is.getMaxStackSize()) {
                return true;
            }
        }
        return player.getInventory().firstEmpty() != -1;
    }
}
