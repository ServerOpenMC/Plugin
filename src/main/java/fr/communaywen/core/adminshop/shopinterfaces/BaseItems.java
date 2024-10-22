package fr.communaywen.core.adminshop.shopinterfaces;

import fr.communaywen.core.adminshop.menu.category.ShopType;

public interface BaseItems {
    String named();
    String getName();
    double getPrize();
    int getSlots();
    ShopType getType();
    int getMaxStack();
}
