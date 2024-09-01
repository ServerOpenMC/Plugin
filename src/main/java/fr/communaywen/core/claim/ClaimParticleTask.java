package fr.communaywen.core.claim;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimParticleTask extends BukkitRunnable {
    private final Player player;
    private final RegionManager region;

    public ClaimParticleTask(Player player, RegionManager regionManager) {
        this.player = player;
        this.region = regionManager;
    }

    @Override
    public void run() {
        showClaimBorders();
    }

    private void showClaimBorders() {
        double minX = region.minLoc.getBlockX() - 0.1;
        double minZ = region.minLoc.getBlockZ() - 0.1;
        double maxX = region.maxLoc.getBlockX() + 1.1;
        double maxZ = region.maxLoc.getBlockZ() + 1.1;
        double y = player.getLocation().getY() + 1;

        for (double x = minX; x <= maxX; x += 0.5) {
            player.spawnParticle(Particle.CHERRY_LEAVES, x, y, minZ, 1, 0, 0, 0, 0);
            player.spawnParticle(Particle.CHERRY_LEAVES, x, y, maxZ, 1, 0, 0, 0, 0);
        }

        for (double z = minZ; z <= maxZ; z += 0.5) {
            player.spawnParticle(Particle.CHERRY_LEAVES, minX, y, z, 1, 0, 0, 0, 0);
            player.spawnParticle(Particle.CHERRY_LEAVES, maxX, y, z, 1, 0, 0, 0, 0);
        }
    }
}
