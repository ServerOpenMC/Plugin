package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBSpawnIllusioner extends LuckyBlockEvent {

    public LBSpawnIllusioner() {
        super(
                "spawn_illusioner",
                "Avada Kedavra !",
                "Un Illusioner est apparu !",
                0.1f,
                EventType.MALUS,
                new ItemStack(Material.BOW)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        world.spawnEntity(block.getLocation(), EntityType.ILLUSIONER);
    }
}
