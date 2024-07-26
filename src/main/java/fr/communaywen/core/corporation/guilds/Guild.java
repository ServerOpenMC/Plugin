package fr.communaywen.core.corporation.guilds;

import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.corporation.guilds.data.MerchantData;
import fr.communaywen.core.corporation.guilds.data.TransactionData;
import fr.communaywen.core.corporation.shops.Shop;
import fr.communaywen.core.corporation.shops.ShopOwner;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import fr.communaywen.core.utils.Queue;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class Guild {

    private final String name;
    private final EconomyManager economyManager;
    private final Map<UUID, MerchantData> merchants = new HashMap<>();
    private final List<Shop> shops = new ArrayList<>();
    private final Queue<Long, TransactionData> transactions = new Queue<>(150);
    private final double turnover = 0;
    private GuildOwner owner;
    private double balance = 0;

    private int shopCounter = 0;

    public Guild(String name, GuildOwner owner, EconomyManager economyManager) {
        this.name = name;
        this.owner = owner;
        this.economyManager = economyManager;
    }

    public double getTurnover() {
        double turnover = 0;
        for (Shop shop : shops) turnover += shop.getTurnover();
        return turnover;
    }

    public Shop getShop(UUID uuid) {
        for (Shop shop : shops) {
            if (shop.getUuid().equals(uuid)) {
                return shop;
            }
        }
        return null;
    }

    public Shop getShop(int shop) {
        for (Shop shopToGet : shops) {
            if (shopToGet.getIndex() == shop) {
                return shopToGet;
            }
        }
        return null;
    }

    public boolean createShop(Player whoCreated) {
        if (withdraw(100, whoCreated, "Création de shop", economyManager)) {
            shops.add(new Shop(new ShopOwner(this), shopCounter, economyManager));
            economyManager.withdrawBalance(whoCreated, 100);
            shopCounter++;
            return true;
        }
        return false;
    }

    public MethodState deleteShop(UUID uuid) {
        for (Shop shop : shops) {
            if (shop.getUuid().equals(uuid)) {
                if (!shop.getItems().isEmpty()) {
                    return MethodState.WARNING;
                }
                shops.remove(shop);
                return MethodState.SUCCESS;
            }
        }
        return MethodState.ERROR;
    }

    public MethodState deleteShop(Player whoCreated, int shop) {
        Shop toDeleteShop = getShop(shop);
        if (toDeleteShop == null) {
            return MethodState.ERROR;
        }
        if (!toDeleteShop.getItems().isEmpty()) {
            return MethodState.WARNING;
        }
        shops.remove(toDeleteShop);
        deposit(75, whoCreated, "Suppression de shop", economyManager);
        economyManager.addBalance(whoCreated.getUniqueId(), 75);
        return MethodState.SUCCESS;
    }

    public List<UUID> getMerchantsUUID() {
        return new ArrayList<>(merchants.keySet());
    }

    public MerchantData getMerchant(UUID uuid) {
        return merchants.get(uuid);
    }

    public void addMerchant(UUID uuid, MerchantData data) {
        merchants.put(uuid, data);
    }

    public void fireMerchant(UUID uuid) {
        removeMerchant(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(ChatColor.RED + "Vous avez été renvoyé de la guilde " + name);
        }
    }

    public void removeMerchant(UUID uuid) {
        merchants.remove(uuid);
    }

    public void broadCastOwner(String message) {
        if (owner.isPlayer()) {
            Player player = Bukkit.getPlayer(owner.getPlayer());
            if (player != null) player.sendMessage(message);
        }
        else {
            for (UUID uuid : owner.getTeam().getPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    continue;
                }
                player.sendMessage(message);
            }
        }
    }

    public boolean isOwner(UUID uuid) {
        if (owner.isPlayer()) {
            return owner.getPlayer().equals(uuid);
        }
        else {
            return owner.getTeam().getPlayers().contains(uuid);
        }
    }

    public boolean isUniqueOwner(UUID uuid) {
        if (owner.isPlayer()) {
            return owner.getPlayer().equals(uuid);
        }
        else {
            return owner.getTeam().getOwner().equals(uuid);
        }
    }

    public boolean isIn(UUID uuid) {
        if (merchants.containsKey(uuid)) {
            return true;
        }
        return isOwner(uuid);
    }

    public void setOwner(UUID uuid) {
        owner = new GuildOwner(uuid);
    }

    public ItemStack getHead() {
        if (owner.isPlayer()) {
            return ItemUtils.getPlayerSkull(owner.getPlayer());
        }
        else {
            return ItemUtils.getPlayerSkull(owner.getTeam().getOwner());
        }
    }

    public boolean withdraw(double amount, Player player, String nature, EconomyManager economyManager) {
        return withdraw(amount, player, nature, "", economyManager);
    }

    public boolean withdraw(double amount, Player player, String nature, String additionalInfo, EconomyManager economyManager) {
        if (balance >= amount) {
            balance -= amount;
            if (amount > 0) {
                TransactionData transaction = new TransactionData(-amount, nature, additionalInfo, player.getUniqueId());
                transactions.add(System.currentTimeMillis(), transaction);
                economyManager.addBalance(player.getUniqueId(), amount);
            }
            return true;
        }
        return false;
    }

    public boolean deposit(double amount, Player player, String nature, EconomyManager economyManager) {
        return deposit(amount, player, nature, "", economyManager);
    }

    public boolean deposit(double amount, Player player, String nature, String additionalInfo, EconomyManager economyManager) {
        if (economyManager.withdrawBalance(player, amount)) {
            balance += amount;
            if (amount > 0) {
                TransactionData transaction = new TransactionData(amount, nature, additionalInfo, player.getUniqueId());
                transactions.add(System.currentTimeMillis(), transaction);
            }
            return true;
        }
        return false;
    }

}
