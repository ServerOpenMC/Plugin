package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlacement implements Listener {
    AdvancementRegister register;

    public BlockPlacement(AdvancementRegister register) {
        this.register = register;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("dreamworld")) { return; }
        if (List.of(Material.ANCIENT_DEBRIS, Material.ENDER_CHEST, Material.SHULKER_BOX).contains(block.getType())) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }
}
