package fr.communaywen.core.luckyblocks.listeners;

import dev.lone.itemsadder.api.CustomBlock;
import dev.xernas.menulib.Menu;
import fr.communaywen.core.luckyblocks.guis.LuckyBlockGUI;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LBPlayerInteractListener implements Listener {

    private final LuckyBlockManager luckyBlockManager;

    public LBPlayerInteractListener(LuckyBlockManager luckyBlockManager) {
        this.luckyBlockManager = luckyBlockManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType().isAir()) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);

        if (customBlock == null) {
            return;
        }

        if (!customBlock.getNamespacedID().equals(LBUtils.getBlockNamespaceID())) {
            return;
        }

        Menu menu = new LuckyBlockGUI(player, luckyBlockManager);
        menu.open();
    }
}
