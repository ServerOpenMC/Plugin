package fr.communaywen.core.elevator;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.elevator.utils.CoordinateManager;
import io.papermc.paper.entity.TeleportFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.sendWarningMessage;

public class ElevatorChunk {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    private static final long TELEPORT_DELAY = 60L;
    private final CoordinateManager coordinateManager;

    public ElevatorChunk(byte[] data) {
        this.coordinateManager = new CoordinateManager(data);
    }

    private static boolean isGoodLocation(World world, Location location) {
        Block block = location.getBlock();
        return checkBlock(block) && checkBlock(block.getRelative(BlockFace.UP));
    }

    private static boolean checkBlock(Block block) {
        Material material = block.getType();
        return material.isAir() ||
                (block.isLiquid() && safeFluid(material)) ||
                block.isPassable() || !material.isCollidable();
    }

    private static boolean safeFluid(Material material) {
        return material != Material.LAVA;
    }

    private static void elevatorTeleport(Player player, Location location, int size, boolean isJump) {
        player.teleport(location, TeleportFlag.Relative.X, TeleportFlag.Relative.Y, TeleportFlag.Relative.Z);
        Component subtitle = Component.text("Vous êtes " + (isJump ? "monté" : "descendu") + " dans le" + (size > 0 ? "s " : " ") + size + " étage" + (size > 1 ? "s" : ""), NamedTextColor.GOLD);
        Component title = Component.text(isJump ? "⬆ En haut ⬆" : "⬇ En bas ⬇", isJump ? NamedTextColor.DARK_GREEN : NamedTextColor.DARK_RED);
        player.playSound(location, Sound.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1, 1);
        Title titleComponent = Title.title(title, subtitle, Title.Times.times(Ticks.duration(5), Ticks.duration(50), Ticks.duration(5)));
        player.showTitle(titleComponent);
    }

    private static void elevatorDelayTeleport(Player player, Location location, int size, boolean isJump, Block block) {
        Component message = Component.text("Vous ne pouvez pas vous téléporter ici !", NamedTextColor.GOLD)
                                     .append(Component.text("\nVous serez téléporté dans " + TELEPORT_DELAY / 20 + " secondes si vous ne bougez pas !", NamedTextColor.YELLOW));
        sendWarningMessage(player, message);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN) == block) {
                elevatorTeleport(player, location, size, isJump);
            }
        }, TELEPORT_DELAY);
    }

    public void unload(Chunk chunk) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        int size = coordinateManager.getSize();
        if (size == 0) {
            container.remove(ElevatorManager.DATA_KEY);
        } else {
            byte[] data = coordinateManager.saveCoordinates();
            if (data != null)
                container.set(ElevatorManager.DATA_KEY, PersistentDataType.BYTE_ARRAY, data);
        }
    }

    public void processJump(Block block, Player player) {
        processElevator(block, player, true);
    }

    public void processElevator(Block block, Player player, boolean isJump) {
        int y = block.getY(), x = block.getX(), z = block.getZ();
        int target = coordinateManager.getTarget(x, y, z, isJump);
        if (target == y) return;
        Location location = new Location(player.getWorld(), x + 0.5, target + 1, z + 0.5);
        int size = coordinateManager.getCount(x, z);
        if (isGoodLocation(player.getWorld(), location)) {
            elevatorTeleport(player, location, size, isJump);
        } else {
            elevatorDelayTeleport(player, location, size, isJump, block);
        }
    }

    public void processSneak(Block block, Player player) {
        processElevator(block, player, false);
    }

    public void placeElevator(Block block) {
        coordinateManager.addY(block.getX(), block.getY(), block.getZ());
    }

    public void removeElevator(Block block) {
        coordinateManager.removeY(block.getX(), block.getY(), block.getZ());
    }
}
