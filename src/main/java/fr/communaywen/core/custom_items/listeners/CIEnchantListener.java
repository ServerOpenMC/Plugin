package fr.communaywen.core.custom_items.listeners;

import fr.communaywen.core.custom_items.managers.CustomItemsManager;
import fr.communaywen.core.custom_items.objects.CustomItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class CIEnchantListener implements Listener {

    private final CustomItemsManager customItemsManager;

    public CIEnchantListener(CustomItemsManager customItemsManager) {
        this.customItemsManager = customItemsManager;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {

        ItemStack item = event.getItem();
        CustomItems customItem = customItemsManager.getCustomItem(item);

        if (customItem == null) {
            return;
        }

        customItem.onEnchant(event);
    }
}
