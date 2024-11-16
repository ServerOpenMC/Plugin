package fr.communaywen.core.event;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import static fr.communaywen.core.event.CubeManager.*;

public class CubeListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Location clickedBlock = event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : null;
        if (clickedBlock != null && isCubeBlock(clickedBlock)) {
            repulsePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && isCubeBlock(event.getEntity().getLocation())) {
            repulsePlayer(player);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isCubeBlock(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location belowPlayer = player.getLocation().clone().subtract(0, 1, 0);

        if (isCubeBlock(belowPlayer)) {
            Vector velocity = player.getVelocity();
            velocity.setY(1.0);
            player.setVelocity(velocity);

            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 2.0f);
        }
    }


    public static boolean isCubeBlock(Location blockLocation) {
        int x = blockLocation.getBlockX();
        int y = blockLocation.getBlockY();
        int z = blockLocation.getBlockZ();

        int cubeX = currentLocation.getBlockX();
        int cubeY = currentLocation.getBlockY();
        int cubeZ = currentLocation.getBlockZ();

        return x >= cubeX && x < cubeX + CUBE_SIZE &&
                y >= cubeY && y < cubeY + CUBE_SIZE &&
                z >= cubeZ && z < cubeZ + CUBE_SIZE &&
                blockLocation.getBlock().getType() == CUBE_MATERIAL;
    }

    private void repulsePlayer(Player player) {
        Vector direction = player.getLocation().toVector().subtract(currentLocation.toVector()).normalize();
        direction.multiply(3);
        direction.setY(1);
        player.setVelocity(direction);

        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 2.0f);
    }
}
