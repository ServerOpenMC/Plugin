package fr.communaywen.core.customitems.managers;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.customitems.items.IronHammer;
import fr.communaywen.core.customitems.objects.CustomItems;

import java.util.ArrayList;

public class CustomItemsManager {

    private final ArrayList<CustomItems> customItems = new ArrayList<>();

    public CustomItemsManager() {
        customItems.add(new IronHammer());
    }

    /**
     * Get the custom item from the custom stack using the namespaced id
     * @param customStack the custom stack
     * @return the custom item
     */
    public CustomItems getCustomItem(CustomStack customStack) {
        for (CustomItems customItem : customItems) {
            if (customItem.getNamespacedID().equals(customStack.getNamespacedID())) {
                return customItem;
            }
        }

        return null;
    }
}
