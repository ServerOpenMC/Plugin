package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Material;

@Credit("Axeno")
@Feature("AdminShop")
public enum OresItems implements BaseItems {
    COAL(13, 3, ShopType.SELL_BUY, "§7Charbon"),
    COPPER_ORE(20, 4, ShopType.SELL_BUY, "§7Lingot de cuivre"),
    IRON_INGOT(21, 8, ShopType.SELL_BUY, "§7Lingot de fer"),
    GOLD_INGOT(22, 12, ShopType.SELL_BUY, "§7Lingot d'or"),
    LAPIS_LAZULI(23, 6, ShopType.SELL_BUY, "§7Lapis lazuli"),
    DIAMOND(24, 20, ShopType.SELL, "§7Diamant"),
    RAW_COPPER(29, 3, ShopType.BUY, "§7Cuivre brut"),
    RAW_IRON(30, 6, ShopType.BUY, "§7Fer brut"),
    RAW_GOLD(31, 10, ShopType.BUY, "§7Or brut"),
    AMETHYST_SHARD(32, 5, ShopType.SELL, "§7Eclat d'améthyste"),
    QUARTZ(33, 4, ShopType.BUY, "§7Quartz"),
    NETHERITE_SCRAP(39, 500000, ShopType.BUY, "§7Fragment de netherite"),
    EMERALD(40, 15, ShopType.SELL, "§7Emeraude"),
    NETHERITE_INGOT(41, 1000000, ShopType.BUY, "§7Lingot de netherite"),
    ;

    private final int slots;
    private final double prize;
    private final ShopType type;
    private final String name;

    OresItems(int slots, double prize, ShopType type, String name) {
        this.slots = slots;
        this.prize = prize;
        this.type = type;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ShopType getType() {
        return this.type;
    }

    @Override
    public double getPrize() {
        return this.prize;
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @Override
    public String named() {
        return name();
    }

    @Override
    public int getMaxStack() {
        Material material = Material.getMaterial(this.named());;
        return material == null ? 64 : material.getMaxStackSize();
    }

}