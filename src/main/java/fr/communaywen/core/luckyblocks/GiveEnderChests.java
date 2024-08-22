package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveEnderChests extends LuckyBlocksEvents{


    public GiveEnderChests() {
        super("GiveEnderChests", "Give 2 enderchests", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        location.getWorld().dropItemNaturally(location, new ItemStack(Material.ENDER_CHEST, 2));
    }
}
