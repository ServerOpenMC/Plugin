package fr.communaywen.core.luckyblocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final LuckyBlockManager lbm = new LuckyBlockManager();

    @EventHandler
    public void onBreak(BlockBreakEvent event){

        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockState bs = block.getState();

        if (bs instanceof Skull){

            Skull skull = (Skull) bs;
            if (skull.getOwner().equalsIgnoreCase("luck")) {
                event.setCancelled(true);
                block.setType(Material.AIR);

                LuckyBlocksEvents randomEvent = lbm.getRandomLB();
                randomEvent.open(player, block);

            }
            }
        }
    }
