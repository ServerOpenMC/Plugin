package fr.communaywen.core.corporation.shops;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Credit("Xernas")
@Feature("Shops")
@Getter
public class Shop {

    private final ShopOwner owner;
    private final EconomyManager economyManager;
    private final List<ShopItem> items = new ArrayList<>();
    private final List<ShopItem> sales = new ArrayList<>();
    private final int index;
    private final UUID uuid = UUID.randomUUID();

    private double turnover = 0;

    public Shop(ShopOwner owner, int index, EconomyManager economyManager) {
        this.owner = owner;
        this.index = index;
        this.economyManager = economyManager;
    }

    public String getName() {
        return "Shop #" + index;
    }

    public boolean isOwner(UUID uuid) {
        if (owner.isGuild()) {
            return owner.getGuild().isOwner(uuid);
        }
        return owner.getPlayer().equals(uuid);
    }

    public MethodState buy(ShopItem item, int amount, Player buyer) {
        if (isFull(buyer)) {
            return MethodState.SPECIAL;
        }
        if (amount > item.getAmount()) {
            return MethodState.WARNING;
        }
        item.setAmount(item.getAmount() - amount);
        buyer.getInventory().addItem(item.getItem());
        turnover += item.getPrice(amount);
        sales.add(item.copy().setAmount(amount));
        if (owner.isGuild()) {
            double price = item.getPrice(amount);
            double guildCut = price * 0.40;
            double supplierCut = price - guildCut;
            owner.getGuild().deposit(guildCut, buyer, "Purchase", getName(), economyManager);
            economyManager.addBalance(item.getSupplier(), supplierCut);
        }
        else {
            if (!economyManager.withdrawBalance(buyer, item.getPrice(amount))) return MethodState.ERROR;
            economyManager.addBalance(owner.getPlayer(), item.getPrice(amount));
        }
        if (item.getAmount() == 0) {
            items.remove(item);
        }
        return MethodState.SUCCESS;
    }

    public ItemBuilder getIcon(Menu menu, boolean fromShopMenu) {
        return new ItemBuilder(menu, fromShopMenu ? Material.GOLD_INGOT : Material.BARREL, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + (fromShopMenu ? "Informations" : getName()));
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "■ Chiffre d'affaires: " + EconomyManager.formatValue(turnover) + "€");
            lore.add(ChatColor.GRAY + "■ Ventes: " + ChatColor.WHITE + sales.size());
            if (!fromShopMenu)
                lore.add(ChatColor.GRAY + "■ Cliquez pour accéder au shop");
            itemMeta.setLore(lore);
        });
    }

    public int getAllItemsAmount() {
        int amount = 0;
        for (ShopItem item : items) {
            amount += item.getAmount();
        }
        return amount;
    }

    private boolean isFull(Player player) {
        for (ItemStack item : getContentsWithoutArmorOrExtras(player)) {
            if (item == null || item.getType() == Material.AIR) {
                return false;
            }
        }
        return true;
    }

    private ItemStack[] getContentsWithoutArmorOrExtras(Player player) {
        // Get player's inventory contents without armor or extras
        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] inventory = new ItemStack[36];
        System.arraycopy(contents, 0, inventory, 0, 36);
        return inventory;
    }

}
