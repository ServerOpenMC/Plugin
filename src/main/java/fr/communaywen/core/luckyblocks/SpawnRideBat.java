package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

public class SpawnRideBat extends LuckyBlocksEvents{

    public SpawnRideBat() {
        super("SpawnRideBat", "Fait spawn une chauve-souris que l'on chevauche", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        Bat bat = (Bat) location.getWorld().spawn(location, Bat.class);
        bat.addPassenger(player);

    }
}
