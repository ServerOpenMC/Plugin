package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.FallingBlocksExplosion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;
import fr.communaywen.core.commands.ExplodeRandomCommand;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionListener implements Listener {

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        if(AywenCraftPlugin.getInstance().getFbeManager().getLocations().contains(event.getBlock().getLocation())) {
            for (Block b: event.blockList()) {
                float x = (float) -5 + (float) (Math.random() * ((5 - -5) + 1));
                float y = (float) -6 + (float) (Math.random() * ((6 - -6) + 1));
                float z = (float) -5 + (float) (Math.random() * ((5 - -5) + 1));

                FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getBlockData());
                fallingBlock.setDropItem(false);
                fallingBlock.setVelocity(new Vector(x, y, z));

                b.setType(Material.AIR);
            }

            AywenCraftPlugin.getInstance().getFbeManager().removeLocation(event.getBlock().getLocation());
        }};

        @EventHandler
        public void onPrimedTNTExplosion(EntityExplodeEvent event) {
            if (ExplodeRandomCommand.preventedExplosvies.contains(event.getEntity())) {
                event.blockList().clear();
                ExplodeRandomCommand.preventedExplosvies.remove(event.getEntity());
            }
        }
    }
