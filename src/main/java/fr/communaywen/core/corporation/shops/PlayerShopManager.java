package fr.communaywen.core.corporation.shops;

import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerShopManager {

    private final Map<UUID, Shop> playerShops = new HashMap<>();
    private final EconomyManager economyManager;

    public PlayerShopManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    public boolean createShop(Player player, Block barrel, Block cashRegister) {
        if (!economyManager.withdrawBalance(player, 500)) {
            return false;
        }
        Shop newShop = new Shop(new ShopOwner(player.getUniqueId()), barrel, cashRegister, 0, economyManager);
        playerShops.put(player.getUniqueId(), newShop);
        newShop.placeShop(player, false);
        return true;
    }

    public MethodState deleteShop(UUID player) {
        Shop shop = getShop(player);
        if (!shop.getItems().isEmpty()) {
            return MethodState.WARNING;
        }
        if (!shop.removeShop()) {
            return MethodState.ESCAPE;
        }
        playerShops.remove(player);
        economyManager.addBalance(player, 400);
        return MethodState.SUCCESS;
    }

    public Shop getShop(UUID player) {
        return playerShops.get(player);
    }

    public boolean hasShop(UUID player) {
        return getShop(player) != null;
    }

}
