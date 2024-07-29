package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;

public enum FoodsItems implements BaseItems {
    COOKED_BEEF(13, 12, ShopType.BUY, "§7Steak"),
    CARROT(20, 12, ShopType.BUY, "§7Carotte"),
    BREAD(21, 12, ShopType.BUY, "§7Pain"),
    APPLE(22, 12, ShopType.BUY, "§7Pomme"),
    SWEET_BERRIES(23, 12, ShopType.BUY, "§7Baies sauvages"),
    COOKED_CHICKEN(24, 12, ShopType.BUY, "§7Poulet cuit"),
    GOLDEN_CARROT(29, 12, ShopType.BUY, "§7Carotte dorée"),
    BAKED_POTATO(30, 12, ShopType.BUY, "§7Patate cuite"),
    COOKED_MUTTON(31, 12, ShopType.BUY, "§7Moutons cuit"),
    GLOW_BERRIES(32, 12, ShopType.BUY, "§7Baies lumineuses"),
    COOKED_RABBIT(33, 12, ShopType.BUY, "§7Lapin cuit"),
    COOKIE(40, 12, ShopType.BUY, "§7Cookie"),
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
}
