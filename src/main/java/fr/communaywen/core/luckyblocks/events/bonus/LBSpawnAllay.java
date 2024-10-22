package fr.communaywen.core.luckyblocks.events.bonus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBSpawnAllay extends LuckyBlockEvent {

    public LBSpawnAllay() {
        super(
                "spawn_allay",
                "Le Schtroumpf volant",
                "Un allay est apparu !",
                0.4f,
                EventType.BONUS,
                new ItemStack(Material.ALLAY_SPAWN_EGG)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        world.spawnEntity(block.getLocation(), EntityType.ALLAY);
    }
}
