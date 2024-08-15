package fr.communaywen.core.elevator;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.managers.ChunkListManager;
import fr.communaywen.core.managers.ChunkManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static fr.communaywen.core.elevator.ElevatorManager.ELEVATOR_NAMESPACE;
import static fr.communaywen.core.elevator.ElevatorManager.isElevator;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.sendFailureMessage;

public class ElevatorListener implements Listener {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();

    @EventHandler
    public void onBlockPlace(CustomBlockPlaceEvent event) {
        Block block = event.getBlock();
        ChunkManager chunkManager = ChunkListManager.getChunk(block.getChunk());
        if (event.getNamespacedID().equals(ELEVATOR_NAMESPACE)) {
            chunkManager.placeElevator(block);
        }
    }

    @EventHandler
    public void onBlockBreak(CustomBlockBreakEvent event) {
        Block block = event.getBlock();
        ChunkManager chunkManager = ChunkListManager.getChunk(block.getChunk());
        if (event.getNamespacedID().equals(ELEVATOR_NAMESPACE)) {
            chunkManager.removeElevator(block);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Block toBlock = event.getTo().getBlock();
        Block fromBlock = event.getFrom().getBlock();
        boolean posChanged = toBlock.getX() != fromBlock.getX() || toBlock.getZ() != fromBlock.getZ();
        if (event.hasChangedBlock() && posChanged) {
            if (ElevatorChunk.removePlayer(event.getPlayer())) {
                sendFailureMessage(event.getPlayer(), "Vous avez bougé, téléportation annulée !");
            }
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        Block block = event.getFrom().getBlock().getRelative(BlockFace.DOWN);
        ChunkManager chunkManager = ChunkListManager.getChunk(block.getChunk());
        if (isElevator(block)) {
            chunkManager.getElevatorManager().processJump(block, player);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        ChunkManager chunkManager = ChunkListManager.getChunk(block.getChunk());
        if (isElevator(block) && event.isSneaking()) {
            if (event.isSneaking()) chunkManager.getElevatorManager().processSneak(block, player);
        }
    }
}
