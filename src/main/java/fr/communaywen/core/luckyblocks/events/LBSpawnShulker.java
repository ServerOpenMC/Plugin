package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBSpawnShulker extends LuckyBlockEvent {

    public LBSpawnShulker() {
        super("Wingardiuuuum Levioosaaaaa", 0.7f);
    }

    public void onOpen(Player player, Block block) {
        block.getWorld().spawnEntity(block.getLocation(), EntityType.SHULKER);
    }
}
