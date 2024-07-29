package fr.communaywen.core.adminshop.menu.category.colored;

import fr.communaywen.core.adminshop.shopinterfaces.ColoredShop;
import org.bukkit.entity.Player;

public enum BlockType {
    CONCRETE("Béton"),
    CONCRETE_POWDER("Béton poudreux"),
    GLASS("Verre"),
    GLASS_PANE("Vitre"),
    TERRACOTTA("Terre cuite"),
    WOOL("Laine")
    ;

    private final String name;
    BlockType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

