package fr.communaywen.core.luckyblocks.events.neutrals;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBSpawnGlowsquid extends LuckyBlockEvent {

    public LBSpawnGlowsquid() {
        super(
                "spawn_glowsquid",
                "Tempête tropicale ?",
                "Un glowsquid tombe du ciel !",
                0.4f,
                EventType.NEUTRAL,
                new ItemStack(Material.GLOW_SQUID_SPAWN_EGG)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        Location location = block.getLocation();
        location.setY(location.getY() + 15);

        world.spawnEntity(location, EntityType.GLOW_SQUID);
    }
}
