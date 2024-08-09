package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DebrisHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("dreamworld")){ return; }
        if (!block.getType().equals(Material.ANCIENT_DEBRIS)) { return; }

        event.setDropItems(false);
        player.getWorld().dropItemNaturally(block.getLocation(),
                CustomStack.getInstance("aywen:dream_essence").getItemStack());
        // TODO: Advancement "Ruée vers le rêve", icone d'Or
    }
}
