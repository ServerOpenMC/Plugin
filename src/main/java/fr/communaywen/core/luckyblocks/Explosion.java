package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Explosion extends LuckyBlocksEvents{


    public Explosion() {
        super("Explosion", "Emet une explosion", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        location.getWorld().createExplosion(location, 5, true);

    }
}
