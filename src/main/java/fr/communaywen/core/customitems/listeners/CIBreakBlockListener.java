package fr.communaywen.core.customitems.listeners;

import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.customitems.objects.CustomItems;
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
