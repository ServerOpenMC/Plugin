package fr.communaywen.core.luckyblocks;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveRTPWand extends LuckyBlocksEvents{


    public GiveRTPWand() {
        super("GiveRTPWand", "Drop un rtp wand", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        location.getWorld().dropItemNaturally(location, new CustomStack(Material)
    }
}
