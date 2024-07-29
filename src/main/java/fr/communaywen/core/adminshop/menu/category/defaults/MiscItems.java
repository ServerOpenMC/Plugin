package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;

@Credit("Axeno")
@Feature("AdminShop")
public enum MiscItems implements BaseItems {

    GLOWSTONE_DUST(13, 12, ShopType.BUY, "§7Poudre lumineuse"),
    BLAZE_ROD(21, 12, ShopType.BUY, "§7Bâton de blaze"),
    ENDER_PEARL(22, 12, ShopType.BUY, "§7Pearl de l'ender"),
    SLIME_BALL(23, 12, ShopType.BUY, "§7Boule de slime"),
    STICK(30, 12, ShopType.BUY, "§7Bâton"),
    NETHER_STAR(31, 12, ShopType.BUY, "§7Etoile du nether"),
    PAPER(32, 12, ShopType.BUY, "§7Papier")
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
}
