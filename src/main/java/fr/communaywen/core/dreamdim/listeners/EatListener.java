package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class EatListener implements Listener {

    AywenCraftPlugin plugin;

    public EatListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodEated(PlayerItemConsumeEvent event) {
        CustomStack stack = CustomStack.byItemStack(event.getItem());

        if (stack != null) {
            if (!stack.getNamespacedID().equals("aywen:somnifere")) { return; }
            this.plugin.getManagers().getDreamdimManager().getUtils().joinDimension(event.getPlayer());
        }
    }
}
