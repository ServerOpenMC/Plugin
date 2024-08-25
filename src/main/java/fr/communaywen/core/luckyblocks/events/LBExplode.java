package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LBExplode extends LuckyBlockEvent {

    public LBExplode() {
        super("Explode", 0.7f);
    }

    public void onOpen(Player player, Block block) {
        block.getWorld().createExplosion(block.getLocation(), 4.0f);
    }
}
