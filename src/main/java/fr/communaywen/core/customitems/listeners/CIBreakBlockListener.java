package fr.communaywen.core.customitems.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.objects.CustomItemsEvents;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CIBreakBlockListener implements Listener {

    private final CustomItemsManager customItemsManager;

    public CIBreakBlockListener(CustomItemsManager customItemsManager) {
        this.customItemsManager = customItemsManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
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

        customItemsEvents.onBlockBreak(event);
    }
}
