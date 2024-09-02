package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Material;

@Credit("Axeno")
@Feature("AdminShop")
public enum MiscItems implements BaseItems {

    GLOWSTONE_DUST(13, 3, ShopType.SELL_BUY, "§7Poudre lumineuse"),
    BLAZE_ROD(21, 4, ShopType.BUY, "§7Bâton de blaze"),
    ENDER_PEARL(22, 8, ShopType.BUY, "§7Pearl de l'ender"),
    SLIME_BALL(23, 6, ShopType.BUY, "§7Boule de slime"),
    STICK(30, 1, ShopType.BUY, "§7Bâton"),
    NETHER_STAR(31, 20200, ShopType.BUY, "§7Etoile du nether"),
    PAPER(32, 3, ShopType.BUY, "§7Papier")
    ;

    private final int slots;
    private final double prize;
    private final ShopType type;
    private final String name;

    MiscItems(int slots, double prize, ShopType type, String name) {
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
