package fr.communaywen.core.luckyblocks.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class AllayEvent implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Location playerLocation = player.getLocation();
        Block block = e.getBlock();
        BlockState bs = block.getState();
        Location location = e.getBlock().getLocation().add(0.5, 0, 0.5);


        if (bs instanceof Skull) {

            Skull skull = (Skull) bs;
            if (skull.getOwner().equalsIgnoreCase("luck")) {
                e.setCancelled(true);
                block.setType(Material.AIR);

                location.getWorld().spawn(location, Allay.class);

            }
        }
    }
}
