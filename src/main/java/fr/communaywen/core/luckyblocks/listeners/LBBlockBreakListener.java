package fr.communaywen.core.luckyblocks.listeners;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBBlockBreakListener implements Listener {

    private final LuckyBlockManager luckyBlockManager;

    public LBBlockBreakListener(LuckyBlockManager luckyBlockManager) {
        this.luckyBlockManager = luckyBlockManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        String itemName = player.getInventory().getItemInMainHand().getType().name();

        if (itemName.toLowerCase().contains("sword") && player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        Block block = event.getBlock();

        if (!LBUtils.canDestroyBlockInClaim(player, block)) {
            event.setCancelled(true);
            return;
        }

        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);

        if (customBlock == null) {
            return;
        }

        if (!customBlock.getNamespacedID().equals(LBUtils.getBlockNamespaceID())) {
            return;
        }

        luckyBlockManager.getRandomEvent().onOpen(player, block);
    }
}
