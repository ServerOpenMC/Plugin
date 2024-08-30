package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import org.bukkit.Material;

public enum FoodsItems implements BaseItems {
    COOKED_BEEF(13, 10, ShopType.BUY, "§7Steak"),
    CARROT(20, 3, ShopType.BUY, "§7Carotte"),
    BREAD(21, 6, ShopType.BUY, "§7Pain"),
    APPLE(22, 12, ShopType.BUY, "§7Pomme"),
    SWEET_BERRIES(23, 2, ShopType.BUY, "§7Baies sauvages"),
    COOKED_CHICKEN(24, 10, ShopType.BUY, "§7Poulet cuit"),
    GOLDEN_CARROT(29, 15, ShopType.BUY, "§7Carotte dorée"),
    BAKED_POTATO(30, 8, ShopType.BUY, "§7Patate cuite"),
    COOKED_MUTTON(31, 10, ShopType.BUY, "§7Moutons cuit"),
    GLOW_BERRIES(32, 3, ShopType.BUY, "§7Baies lumineuses"),
    COOKED_RABBIT(33, 10, ShopType.BUY, "§7Lapin cuit"),
    COOKIE(40, 5, ShopType.BUY, "§7Cookie"),
    ;

    private final int slots;
    private final double prize;
    private final ShopType type;
    private final String name;

    FoodsItems(int slots, double prize, ShopType type, String name) {
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
