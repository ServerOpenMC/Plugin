package fr.communaywen.core.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ParticleRegionManager {


    static JavaPlugin plugin;
    public ParticleRegionManager(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    public static void spawnParticlesInRegion(String regionId, World world, Particle particle) {
        RegionManager regionManager =  WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) return;

        ProtectedRegion region = regionManager.getRegion(regionId);
        if (region == null) return;

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        Location minLocation = new Location(world, min.x(), min.y(), min.z());
        Location maxLocation = new Location(world, max.x(), max.y(), max.z());

        new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();

                for (int i = 0; i < 100; i++) {
                    double x = minLocation.getX() + random.nextDouble() * (maxLocation.getX() - minLocation.getX());
                    double y = minLocation.getY() + random.nextDouble() * (150 - minLocation.getY());
                    double z = minLocation.getZ() + random.nextDouble() * (maxLocation.getZ() - minLocation.getZ());

                    Location particleLocation = new Location(world, x, y, z);
                    world.spawnParticle(particle, particleLocation, 1);
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
