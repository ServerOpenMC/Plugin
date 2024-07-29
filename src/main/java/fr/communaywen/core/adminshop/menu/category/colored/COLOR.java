package fr.communaywen.core.adminshop.menu.category.colored;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;

public enum COLOR implements BaseItems {
    WHITE(12, 1, ShopType.BUY, "§7blanc"),
    LIGHT_GRAY(13, 1, ShopType.BUY, "§7gris clair"),
    GRAY(14, 1, ShopType.BUY, "§7gris"),
    BLACK(20, 1, ShopType.BUY, "§7noir"),
    RED(21, 1, ShopType.BUY, "§7orange"),
    ORANGE(22, 1, ShopType.BUY, "§7Blanche"),
    YELLOW(23, 1, ShopType.BUY, "§7jaune"),
    LIME(24, 1, ShopType.BUY, "§7vert clair"),
    BROWN(29, 1, ShopType.BUY, "§7marron"),
    CYAN(30, 1, ShopType.BUY, "§7cyan"),
    LIGHT_BLUE(31, 1, ShopType.BUY, "§7bleu clair"),
    BLUE(32, 1, ShopType.BUY, "§7bleu"),
    GREEN(33, 1, ShopType.BUY, "§7vert"),
    PURPLE(39, 1, ShopType.BUY, "§7violet"),
    MAGENTA(40, 1, ShopType.BUY, "§7magenta"),
    PINK(41, 1, ShopType.BUY, "§7rose"),
    ;

    private final int slots;
    private final double prize;
    private final ShopType type;
    private final String name;
    COLOR(int slots, double prize, ShopType type, String name) {
        this.slots = slots;
        this.prize = prize;
        this.type = type;
        this.name = name;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public ShopType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrize() {
        return prize;
    }

    @Override
    public String named() {
        return name();
    }
}
