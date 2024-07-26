package fr.communaywen.core.corporation.shops;

import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerShopManager {

    private final Map<UUID, Shop> playerShops = new HashMap<>();
    private final EconomyManager economyManager;

    public PlayerShopManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    public boolean createShop(Player player) {
        if (!economyManager.withdrawBalance(player, 500)) {
            return false;
        }
        playerShops.put(player.getUniqueId(), new Shop(new ShopOwner(player.getUniqueId()), 0, economyManager));
        return true;
    }

    public void deleteShop(UUID player) {
        playerShops.remove(player);
        economyManager.addBalance(player, 400);
    }

    public Shop getShop(UUID player) {
        return playerShops.get(player);
    }

    public boolean hasShop(UUID player) {
        return getShop(player) != null;
    }

}
