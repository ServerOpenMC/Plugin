package fr.communaywen.core.luckyblocks.events.bonus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBEnderchest extends LuckyBlockEvent {

    public LBEnderchest() {
        super(
                "enderchest",
                "Toujours connectés",
                "2 enderchests sont apparus !",
                0.5f,
                EventType.BONUS,
                new ItemStack(Material.ENDER_CHEST)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        ItemStack enderchest = new ItemStack(Material.ENDER_CHEST, 2);

        world.dropItemNaturally(block.getLocation(), enderchest);
    }
}
