package fr.communaywen.core.adminshop.shopinterfaces;

import fr.communaywen.core.adminshop.menu.category.ShopType;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;

@Credit("Axeno")
@Feature("AdminShop")
public interface BaseItems {
    String named();
    String getName();
    double getPrize();
    int getSlots();
    ShopType getType();
    int getMaxStack();
}
