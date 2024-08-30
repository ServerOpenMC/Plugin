package fr.communaywen.core.adminshop.menu.category.colored;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import org.bukkit.Material;

public enum COLOR implements BaseItems {
    WHITE(12, 2, ShopType.BUY, "§7blanc"),
    LIGHT_GRAY(13, 2, ShopType.BUY, "§7gris clair"),
    GRAY(14, 2, ShopType.BUY, "§7gris"),
    BLACK(20, 2, ShopType.BUY, "§7noir"),
    RED(21, 2, ShopType.BUY, "§7orange"),
    ORANGE(22, 2, ShopType.BUY, "§7Blanche"),
    YELLOW(23, 2, ShopType.BUY, "§7jaune"),
    LIME(24, 2, ShopType.BUY, "§7vert clair"),
    BROWN(29, 2, ShopType.BUY, "§7marron"),
    CYAN(30, 2, ShopType.BUY, "§7cyan"),
    LIGHT_BLUE(31, 2, ShopType.BUY, "§7bleu clair"),
    BLUE(32, 2, ShopType.BUY, "§7bleu"),
    GREEN(33, 2, ShopType.BUY, "§7vert"),
    PURPLE(39, 2, ShopType.BUY, "§7violet"),
    MAGENTA(40, 2, ShopType.BUY, "§7magenta"),
    PINK(41, 2, ShopType.BUY, "§7rose"),
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

    @Override
    public int getMaxStack() {
        Material material = Material.getMaterial(this.named());;
        return material == null ? 64 : material.getMaxStackSize();
    }
}
