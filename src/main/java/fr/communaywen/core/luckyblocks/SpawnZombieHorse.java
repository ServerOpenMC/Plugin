package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieHorse;

public class SpawnZombieHorse extends LuckyBlocksEvents{


    public SpawnZombieHorse() {
        super("SpawnZombieHorse", "Fais spawn un cheval-zombie", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        ZombieHorse zombieHorse = (ZombieHorse) location.getWorld().spawn(location, ZombieHorse.class);

    }
}
