package fr.communaywen.core.guideline.listeners.dream;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.PlayerInventory;

public class CookedDreamFish implements Listener {
    @EventHandler
    public void onFishCooked(InventoryMoveItemEvent event) {
        if (!((event.getSource() instanceof FurnaceInventory))) return;
        if (!((event.getDestination() instanceof PlayerInventory playerInventory))) return;

        CustomStack customStack = CustomStack.byItemStack(event.getItem());

        if (customStack == null) return;
        Player player = (Player) playerInventory.getHolder();

        switch (customStack.getNamespacedID()) {
            case "cooked_poissonion":
                GuidelineManager.getAPI().getAdvancement("dream:cooked_poissonion").grant(player);
            case "sun_fish":
                GuidelineManager.getAPI().getAdvancement("dream:sunfish").grant(player);
        }
    }
}
