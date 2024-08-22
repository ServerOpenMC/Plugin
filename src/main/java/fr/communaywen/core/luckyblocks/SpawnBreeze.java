package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.Player;

public class SpawnBreeze extends LuckyBlocksEvents{


    public SpawnBreeze() {
        super("SpawnBreeze", "Fait apparaitre un breeze", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        Breeze breeze = (Breeze) location.getWorld().spawn(location, Breeze.class);
    }
}
