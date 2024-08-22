package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;

public class SpawnAllay extends LuckyBlocksEvents{

    public SpawnAllay() {
        super("SpawnAllay", "Fais spawn un allay", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        Allay allay = location.getWorld().spawn(location, Allay.class);


    }
}
