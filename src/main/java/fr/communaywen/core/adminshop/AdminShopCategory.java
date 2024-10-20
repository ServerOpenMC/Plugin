package fr.communaywen.core.adminshop;

import fr.communaywen.core.adminshop.menu.category.defaults.BlocksItems;
import fr.communaywen.core.adminshop.menu.category.defaults.AdminShopDefaults;
import fr.communaywen.core.adminshop.menu.category.defaults.FoodsItems;
import fr.communaywen.core.adminshop.menu.category.defaults.MiscItems;
import fr.communaywen.core.adminshop.menu.category.defaults.OresItems;
import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.shopinterfaces.BaseShop;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
public enum AdminShopCategory {
    BLOCKS(Material.GRASS_BLOCK, "§a§lBlocks", BlocksItems.values()),
    ORES(Material.IRON_INGOT, "§6§lOres", OresItems.values()),
    FOODS(Material.COOKED_BEEF, "§c§lNourritures", FoodsItems.values()),
    MISC(Material.BLAZE_ROD, "§8§lMisc", MiscItems.values()),
    ;

    private final Material blocks;
    private final String name;
    private final BaseItems[] items;
    AdminShopCategory(Material blocks, String name, BaseItems[] items) {
        this.blocks = blocks;
        this.name = name;
        this.items = items;
    }

    public BaseShop createMenu(Player player) {
        return new AdminShopDefaults(player, this);
    }
}
