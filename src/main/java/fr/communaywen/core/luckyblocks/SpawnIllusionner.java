package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Player;

public class SpawnIllusionner extends LuckyBlocksEvents{


    public SpawnIllusionner() {
        super("SpawnIllusionner", "Fais spawn un illusioner", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        Illusioner illusioner = (Illusioner) location.getWorld().spawn(location, Illusioner.class);

    }
}
