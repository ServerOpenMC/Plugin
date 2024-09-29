package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class HoeListener implements Listener {

    @EventHandler
    public void onCropBreaked(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();

        ItemStack mainhand = player.getInventory().getItemInMainHand();

        try {
            if (!mainhand.getPersistentDataContainer().get(NamespacedKey.fromString("replenish", AywenCraftPlugin.getInstance()), PersistentDataType.BOOLEAN)) return;
        } catch (Exception e) {
            return;
        }
        // TODO
    }
}
