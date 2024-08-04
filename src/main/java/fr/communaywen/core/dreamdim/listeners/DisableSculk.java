package fr.communaywen.core.dreamdim.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DisableSculk implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getWorld().getName().equals("dreamworld")) { return; }

        if (event.getBlock().getType() == Material.SCULK) {
            event.setExpToDrop(0);
            event.setDropItems(false);
        }
    }
}
