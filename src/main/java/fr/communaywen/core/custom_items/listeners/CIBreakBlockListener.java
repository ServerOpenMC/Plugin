package fr.communaywen.core.custom_items.listeners;

import fr.communaywen.core.custom_items.managers.CustomItemsManager;
import fr.communaywen.core.custom_items.objects.CustomItems;
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
        CustomItems customItem = customItemsManager.getCustomItem(item);

        if (customItem == null) {
            return;
        }

        customItem.onBlockBreak(event);
    }
}
