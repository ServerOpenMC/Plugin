package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;

public class SpawnSkeletonHorse extends LuckyBlocksEvents{


    public SpawnSkeletonHorse() {
        super("SpawnSkeletonHorse", "Fais Spawn un cheval squelette", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        SkeletonHorse skeletonHorse = (SkeletonHorse) location.getWorld().spawn(location, SkeletonHorse.class);
    }
}
