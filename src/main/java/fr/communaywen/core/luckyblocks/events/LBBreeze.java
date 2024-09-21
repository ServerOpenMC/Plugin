package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBBreeze extends LuckyBlockEvent {

    public LBBreeze() {
        super(
                "Breeze",
                "§7Un vent de fraîcheur souffle sur vous.",
                0.2f,
                EventType.MALUS,
                new ItemStack(Material.BREEZE_ROD)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        world.spawnEntity(block.getLocation(), EntityType.BREEZE);
    }
}
