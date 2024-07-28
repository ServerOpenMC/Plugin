package fr.communaywen.core.corporation.guilds;

import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.corporation.guilds.data.MerchantData;
import fr.communaywen.core.corporation.guilds.data.TransactionData;
import fr.communaywen.core.corporation.shops.Shop;
import fr.communaywen.core.corporation.shops.ShopOwner;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import fr.communaywen.core.utils.Queue;
import fr.communaywen.core.utils.WorldUtils;
import fr.communaywen.core.utils.Yaw;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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

    public boolean createShop(Player whoCreated, Block barrel, Block cashRegister) {
        if (withdraw(100, whoCreated, "Création de shop", economyManager)) {
            Shop newShop = new Shop(new ShopOwner(this), barrel, cashRegister, shopCounter, economyManager);
            shops.add(newShop);
            economyManager.withdrawBalance(whoCreated, 100);
            newShop.placeShop(whoCreated, true);
            shopCounter++;
            return true;
        }
        return false;
    }

    public MethodState deleteShop(Player player, UUID uuid) {
        for (Shop shop : shops) {
            if (shop.getUuid().equals(uuid)) {
                if (!shop.getItems().isEmpty()) {
                    return MethodState.WARNING;
                }
                if (!shop.removeShop()) {
                    return MethodState.ESCAPE;
                }
                if (!deposit(75, player, "Suppression de shop", economyManager)) {
                    return MethodState.SPECIAL;
                };
                shops.remove(shop);
                economyManager.addBalance(player.getUniqueId(), 75);
                return MethodState.SUCCESS;
            }
        }
        return MethodState.ERROR;
    }

    public List<UUID> getAllMembers() {
        List<UUID> members = new ArrayList<>();
        if (owner.isPlayer()) {
            members.add(owner.getPlayer());
        }
        else {
            members.addAll(owner.getTeam().getPlayers());
        }
        members.addAll(merchants.keySet());
        return members;
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
