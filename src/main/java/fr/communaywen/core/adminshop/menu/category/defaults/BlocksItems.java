package fr.communaywen.core.adminshop.menu.category.defaults;

import fr.communaywen.core.adminshop.shopinterfaces.BaseItems;
import fr.communaywen.core.adminshop.menu.category.ShopType;
import org.bukkit.Material;

public enum BlocksItems implements BaseItems {
    WHITE_CONCRETE_POWDER(12, 2, ShopType.WHITE_CONCRETE_POWDER, "§7Béton en poudre"),
    WHITE_WOOL(13, 2, ShopType.WHITE_WOOL, "§7Laine"),
    WHITE_CONCRETE(14, 2, ShopType.WHITE_CONCRETE, "§7Béton"),
    GRASS_BLOCK(19, 3, ShopType.SELL_BUY, "§7Bloc d'herbe"),
    STONE(20, 2, ShopType.SELL_BUY, "§7Pierre"),
    DIORITE(21, 2, ShopType.SELL, "§7Diorite"),
    DEEPSLATE(22, 3, ShopType.BUY, "§7Ardoise des abîmes"),
    ICE(23, 4, ShopType.BUY, "§7Glace"),
    AMETHYST_BLOCK(24, 20, ShopType.BUY, "§7Bloc d'améthyste"),
    OAK_LEAVES(25, 1, ShopType.SELL, "§7Feuilles de chêne"),
    DIRT(28, 1, ShopType.BUY, "§7Terre"),
    GRAVEL(29, 2, ShopType.SELL, "§7Gravier"),
    GRANITE(30, 2, ShopType.SELL, "§7Granite"),
    OBSIDIAN(31, 10, ShopType.SELL, "§7Obsidienne"),
    PACKED_ICE(32, 2, ShopType.SELL, "§7Glace compactée"),
    MOSS_BLOCK(33, 0.75, ShopType.SELL, "§7Bloc de mousse"),
    FLOWERING_AZALEA_LEAVES(34, 3, ShopType.SELL, "§7Feuilles d'azalée fleurie"),
    TERRACOTTA(39, 2, ShopType.TERRACOTTA, "§7Terre cuite"),
    GLASS(40, 2, ShopType.GLASS, "§7Verre"),
    GLASS_PANE(41, 2, ShopType.GLASS_PANE, "§7Vitre"),
    ;

    private final int slots;
    private final double prize;
    private final ShopType type;
    private final String name;

    BlocksItems(int slots, double prize, ShopType type, String name) {
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
