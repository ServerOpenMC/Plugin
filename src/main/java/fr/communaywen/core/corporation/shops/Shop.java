package fr.communaywen.core.corporation.shops;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import fr.communaywen.core.utils.WorldUtils;
import fr.communaywen.core.utils.Yaw;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Credit("Xernas")
@Feature("Shops")
@Getter
public class Shop {

    private final ShopOwner owner;
    private final EconomyManager economyManager;
    private final List<ShopItem> items = new ArrayList<>();
    private final List<ShopItem> sales = new ArrayList<>();
    private final Map<UUID, UUID> suppliers = new HashMap<>();
    private final int index;
    private final UUID uuid = UUID.randomUUID();
    private final Block cashBlock;
    private final Block stockBlock;

    private double turnover = 0;

    public Shop(ShopOwner owner, Block stockBlock, Block cashBlock, int index, EconomyManager economyManager) {
        this.owner = owner;
        this.stockBlock = stockBlock;
        this.cashBlock = cashBlock;
        this.index = index;
        this.economyManager = economyManager;
    }

    public String getName() {
        return owner.isGuild() ? ("Shop #" + index) : Bukkit.getOfflinePlayer(owner.getPlayer()).getName() + "'s Shop";
    }

    public UUID getSupremeOwner() {
        return owner.isGuild() ? owner.getGuild().getOwner().getPlayer() : owner.getPlayer();
    }

    public boolean isOwner(UUID uuid) {
        if (owner.isGuild()) {
            return owner.getGuild().isOwner(uuid);
        }
        return owner.getPlayer().equals(uuid);
    }

    public boolean addItem(ItemStack itemStack, double price) {
        ShopItem item = new ShopItem(itemStack, price);
        for (ShopItem shopItem : items) {
            if (shopItem.getItem().isSimilar(itemStack)) {
                return true;
            }
        }
        items.add(item);
        return false;
    }

    public ShopItem getItem(ShopItem shopItem) {
        for (ShopItem item : items) {
            if (item.equals(shopItem)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(ShopItem item) {
        items.remove(item);
    }

    public void supply(ItemStack item, UUID supplier) {
        for (ShopItem shopItem : items) {
            if (shopItem.getItem().isSimilar(item)) {
                shopItem.setAmount(shopItem.getAmount() + item.getAmount());
                suppliers.put(shopItem.getItemID(), supplier);
            }
        }
    }

    public MethodState buy(ShopItem item, int amount, Player buyer) {
        if (isFull(buyer)) {
            return MethodState.SPECIAL;
        }
        if (amount > item.getAmount()) {
            return MethodState.WARNING;
        }
        item.setAmount(item.getAmount() - amount);
        turnover += item.getPrice(amount);
        if (owner.isGuild()) {
            double price = item.getPrice(amount);
            double guildCut = price * 0.40;
            double supplierCut = price - guildCut;
            UUID supplier = suppliers.get(item.getItemID());
            if (supplier == null) {
                return MethodState.ESCAPE;
            }
            owner.getGuild().deposit(guildCut, buyer, "Purchase", getName(), economyManager);
            economyManager.addBalance(supplier, supplierCut);
        }
        else {
            if (!economyManager.withdrawBalance(buyer, item.getPrice(amount))) return MethodState.ERROR;
            economyManager.addBalance(owner.getPlayer(), item.getPrice(amount));
        }
        //TODO Give certain amount of that item to the buyer
        buyer.getInventory().addItem(item.getItem());
        sales.add(item.copy().setAmount(amount));
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

    public static UUID getShopPlayerLookingAt(Player player, boolean isGuild) {
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null || targetBlock.getType() != Material.BARREL) {
            return null;
        }
        Barrel barrel = (Barrel) targetBlock.getState();
        String shopUUID = barrel.getPersistentDataContainer().get(isGuild ? AywenCraftPlugin.GUILD_SHOP_KEY : AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING);
        if (shopUUID == null) {
            return null;
        }
        return UUID.fromString(shopUUID);
    }

    public void placeShop(Player player, boolean isGuild) {
        Yaw yaw = WorldUtils.getYaw(player);
        //TODO ItemsAdder cash register
        cashBlock.setType(Material.OAK_SIGN);
        BlockData cashData = cashBlock.getBlockData();
        if (cashData instanceof Directional directional) {
            directional.setFacing(yaw.getOpposite().toBlockFace());
            cashBlock.setBlockData(directional);
        }
        Barrel barrel = (Barrel) stockBlock.getState();
        barrel.getPersistentDataContainer().set(isGuild ? AywenCraftPlugin.GUILD_SHOP_KEY : AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING, getUuid().toString());
        barrel.update();
        Sign sign = (Sign) cashBlock.getState();
        sign.getPersistentDataContainer().set(isGuild ? AywenCraftPlugin.GUILD_SHOP_KEY : AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING, getUuid().toString());
        sign.update();
    }

    public boolean removeShop() {
        //TODO ItemsAdder cash register
        if (cashBlock == null || stockBlock == null || cashBlock.getType() != Material.OAK_SIGN || stockBlock.getType() != Material.BARREL) {
            return false;
        }
        Sign sign = (Sign) cashBlock.getState();
        if (sign.getPersistentDataContainer().has(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)) {
            sign.getPersistentDataContainer().remove(AywenCraftPlugin.GUILD_SHOP_KEY);
        }
        else {
            sign.getPersistentDataContainer().remove(AywenCraftPlugin.PLAYER_SHOP_KEY);
        }
        cashBlock.setType(Material.AIR);
        sign.update();
        Barrel barrel = (Barrel) stockBlock.getState();
        if (barrel.getPersistentDataContainer().has(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)) {
            barrel.getPersistentDataContainer().remove(AywenCraftPlugin.GUILD_SHOP_KEY);
        }
        else {
            barrel.getPersistentDataContainer().remove(AywenCraftPlugin.PLAYER_SHOP_KEY);
        }
        barrel.update();
        return true;
    }

}
