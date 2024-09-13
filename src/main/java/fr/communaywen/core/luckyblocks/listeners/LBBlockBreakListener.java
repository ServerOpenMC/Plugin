package fr.communaywen.core.luckyblocks.listeners;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBBlockBreakListener implements Listener {

    private final LuckyBlockManager luckyBlockManager;

    public LBBlockBreakListener(LuckyBlockManager luckyBlockManager) {
        this.luckyBlockManager = luckyBlockManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);

        if (customBlock == null) {
            return;
        }

        if (!customBlock.getNamespacedID().equals(LBUtils.getBlockNamespaceID())) {
            return;
        }

        Player player = event.getPlayer();
        luckyBlockManager.getRandomEvent().onOpen(player, block);
    }
}
