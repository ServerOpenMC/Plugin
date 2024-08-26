package fr.communaywen.core.customitems.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.objects.CustomItemsEvents;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
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

        CustomStack customStack = CustomStack.byItemStack(item);

        if (customStack == null) {
            return;
        }

        if (!customStack.getNamespacedID().startsWith(CustomItemsUtils.getNamespaceStart())) {
            return;
        }

        CustomItems customItems = customItemsManager.getCustomItem(customStack);

        if (customItems == null) {
            return;
        }

        if (!(customItems instanceof CustomItemsEvents customItemsEvents)) {
            return;
        }

        customItemsEvents.onAnvil(event);
    }
}
