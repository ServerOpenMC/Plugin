package fr.communaywen.core.custom_items.listeners;

import fr.communaywen.core.custom_items.managers.CustomItemsManager;
import fr.communaywen.core.custom_items.objects.CustomItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class CIPrepareAnvilListener implements Listener {

    private final CustomItemsManager customItemsManager;

    public CIPrepareAnvilListener(CustomItemsManager customItemsManager) {
        this.customItemsManager = customItemsManager;
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {

        ItemStack item = event.getInventory().getItem(0);

        if (item == null) {
            return;
        }

        CustomItems customItem = customItemsManager.getCustomItem(item);

        if (customItem == null) {
            return;
        }

        customItem.onAnvil(event);
    }
}
