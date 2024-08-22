package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Lightning extends LuckyBlocksEvents{

    public Lightning() {
        super("Lightning", "Invoque la foudre", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        location.getWorld().strikeLightningEffect(location);

    }
}
