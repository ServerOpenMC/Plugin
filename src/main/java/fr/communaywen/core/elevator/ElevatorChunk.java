package fr.communaywen.core.elevator;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.elevator.utils.CoordinateManager;
import io.papermc.paper.entity.TeleportFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.sendWarningMessage;

public class ElevatorChunk {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    private static final long TELEPORT_DELAY = 60L;
    private static final Map<Player, BukkitRunnable> teleportingPlayers = new HashMap<>();
    private final CoordinateManager coordinateManager;

    public ElevatorChunk(byte[] data) {
        this.coordinateManager = new CoordinateManager(data);
    }

    private static boolean isGoodLocation(Location location, Player player) {
        Block block = location.getBlock();
        boolean ground = checkBlock(block);
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_SCALE);
        if (ground && attribute != null && attribute.getBaseValue() * 1.8 <= 1) return true;
        return ground && checkBlock(block.getRelative(BlockFace.UP));
    }

    private static boolean checkBlock(Block block) {
        Material material = block.getType();
        return material != Material.LAVA && (material.isAir() ||
                block.isLiquid() ||
                block.isPassable() || !material.isCollidable());
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
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                teleportingPlayers.remove(player);
                elevatorTeleport(player, location, size, isJump);
            }
        };
        runnable.runTaskLater(plugin, TELEPORT_DELAY);
        teleportingPlayers.put(player, runnable);
    }

    public static boolean removePlayer(Player player) {
        BukkitRunnable runnable = teleportingPlayers.remove(player);
        if (runnable != null) {
            runnable.cancel();
            return true;
        } else return false;
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
        if (teleportingPlayers.containsKey(player)) return;
        int y = block.getY(), x = block.getX(), z = block.getZ();
        int target = coordinateManager.getTarget(x, y, z, isJump);
        if (target == y) return;
        Location location = player.getLocation().clone().set(x + 0.5, target + 1, z + 0.5);
        int size = coordinateManager.getCount(x, z);
        if (isGoodLocation(location, player)) {
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
