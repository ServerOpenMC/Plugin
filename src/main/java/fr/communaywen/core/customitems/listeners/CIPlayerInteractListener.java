package fr.communaywen.core.customitems.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.objects.CustomItemsEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CIPlayerInteractListener implements Listener {

    private final CustomItemsManager customItemsManager;

    public CIPlayerInteractListener(CustomItemsManager customItemsManager) {
        this.customItemsManager = customItemsManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType().isAir()) {
            return;
        }

        CustomStack customStack = CustomStack.byItemStack(itemStack);

        if (customStack == null) {
            return;
        }

        CustomItems customItems = customItemsManager.getCustomItem(customStack);

        if (customItems == null) {
            return;
        }

        if (!(customItems instanceof CustomItemsEvents customItemsEvents)) {
            return;
        }

        customItemsEvents.onInteract(event);
    }
}
