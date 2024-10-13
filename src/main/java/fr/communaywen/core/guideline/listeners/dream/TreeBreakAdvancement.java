package fr.communaywen.core.guideline.listeners.dream;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TreeBreakAdvancement implements Listener {
    public boolean is(Block block, String namespacedId) {
        CustomBlock cb = CustomBlock.byAlreadyPlaced(block);
        return cb != null && cb.getNamespacedID().equals(namespacedId);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (is(block, "aywen:dream_log")) {
            GuidelineManager.getAPI().getAdvancement("dream:wood").grant(player);
        } else if (is(block, "aywen:cloud")) {
            GuidelineManager.getAPI().getAdvancement("dream:cloud").grant(player);
        }
    }
}
