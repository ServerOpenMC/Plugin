package fr.communaywen.core.customitems.managers;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.customitems.items.BuilderWand;
import fr.communaywen.core.customitems.items.DiamondHammer;
import fr.communaywen.core.customitems.items.IronHammer;
import fr.communaywen.core.customitems.items.NetheriteHammer;
import fr.communaywen.core.customitems.objects.CustomItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsManager {

    private final ArrayList<CustomItems> customItems = new ArrayList<>();

    static AywenCraftPlugin plugin;
    public CustomItemsManager(AywenCraftPlugin plugin, FileConfiguration customItemsConfig) {

        customItems.add(new IronHammer(plugin));
        customItems.add(new DiamondHammer(plugin));
        customItems.add(new NetheriteHammer(plugin));
        customItems.add(new BuilderWand(plugin, customItemsConfig));

        initCustomItems();
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

    public ArrayList<CustomItems> getCustomItems() {
        return customItems;
    }

    private void initCustomItems() {

        List<CustomStack> itemsAdderNamespaces = ItemsAdder.getAllItems();

        if (itemsAdderNamespaces == null) {
            return;
        }

        List<CustomItems> customItems = getCustomItems();

        for (CustomStack customStack : itemsAdderNamespaces) {
            for (CustomItems customItem : customItems) {
                if (customItem.getNamespacedID().equals(customStack.getNamespacedID())) {
                    ItemStack itemStack = customStack.getItemStack();
                    customItem.setName(itemStack.getItemMeta().getDisplayName());
                    customItem.setItemStack(itemStack);
                }
            }
        }
    }
}
