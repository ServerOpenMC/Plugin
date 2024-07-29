package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.AdminShopCategory;
import fr.communaywen.core.adminshop.shopinterfaces.BaseShop;
import org.bukkit.entity.Player;

public class AdminShopDefaults extends BaseShop {
    public AdminShopDefaults(Player player, AdminShopCategory category) {
        super(player, "§6Admin Shop - " + category.getName(), category.getItems());
    }
}
