package fr.communaywen.core.luckyblocks;

import fr.communaywen.core.luckyblocks.events.ExplosionEvent;
import fr.communaywen.core.luckyblocks.events.LightningEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class LuckyBlockEventDispatcher implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        BlockState bs = block.getState();
        Location location = e.getBlock().getLocation().add(0.5, 0, 0.5);

        if (bs instanceof Skull) {
            Skull skull = (Skull) bs;
            if (skull.getOwner().equalsIgnoreCase("luck")) {
                e.setCancelled(true);
                block.setType(org.bukkit.Material.AIR);

                Random random = new Random();
                int alea = random.nextInt(2);

                if (alea == 0) {
                    new ExplosionEvent().trigger(location);
                }
                if (alea == 1) {
                    new LightningEvent().trigger(location);

                }
                }
            }
        }
    }
