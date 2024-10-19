package fr.communaywen.core.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.contest.managers.ColorConvertor;
import fr.communaywen.core.managers.RegionsManager;
import fr.communaywen.core.spawn.jump.JumpManager;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

import static fr.communaywen.core.managers.LeaderboardManager.*;


public class ParticleListener implements Listener {
    private BukkitRunnable eventRunnable;

    public ParticleListener(AywenCraftPlugin plugin) {

        World world = Bukkit.getWorld(plugin.getConfig().getString("spawn.world"));
        String regionId = plugin.getConfig().getString("spawn.region");

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
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
                if (RegionsManager.isPlayerInRegion(regionId, world) && Bukkit.getOnlinePlayers().size() >= 1) {
                    Random random = new Random();

                    if (ContestCache.getPhaseCache() != 1) {
                        //PARTICULE POUR CONTEST
                        ChatColor color1 = ChatColor.valueOf(ContestCache.getColor1Cache());
                        ChatColor color2 = ChatColor.valueOf(ContestCache.getColor2Cache());

                        int[] rgb1 = ColorConvertor.getRGBFromChatColor(color1);
                        int[] rgb2 = ColorConvertor.getRGBFromChatColor(color2);

                        for (int i = 0; i < 100; i++) {
                            // COULEUR CAMP 1
                            double x = minLocation.getX() + random.nextDouble() * (maxLocation.getX() - minLocation.getX());
                            double y = minLocation.getY() + random.nextDouble() * (85 - 60);
                            double z = minLocation.getZ() + random.nextDouble() * (maxLocation.getZ() - minLocation.getZ());

                            Location particleLocation = new Location(world, x, y, z);

                            world.spawnParticle(Particle.ENTITY_EFFECT, particleLocation, 0, Color.fromRGB(rgb1[0], rgb1[1], rgb1[2]));

                            // COULEUR CAMP 2
                            double x2 = minLocation.getX() + random.nextDouble() * (maxLocation.getX() - minLocation.getX());
                            double y2 = minLocation.getY() + random.nextDouble() * (85 - 60);
                            double z2 = minLocation.getZ() + random.nextDouble() * (maxLocation.getZ() - minLocation.getZ());

                            Location particleLocation2 = new Location(world, x2, y2, z2);

                            world.spawnParticle(Particle.ENTITY_EFFECT, particleLocation2, 0, Color.fromRGB(rgb2[0], rgb2[1], rgb2[2]));
                        }
                    } else {
                        // PARTICULE DU SPAWN PAR DEFAUT
                        for (int i = 0; i < 120; i++) {
                            double x = minLocation.getX() + random.nextDouble() * (maxLocation.getX() - minLocation.getX());
                            double y = minLocation.getY() + random.nextDouble() * (130 - 60);
                            double z = minLocation.getZ() + random.nextDouble() * (maxLocation.getZ() - minLocation.getZ());

                            Location particleLocation = new Location(world, x, y, z);

                            world.spawnParticle(Particle.CHERRY_LEAVES, particleLocation, 1);

                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
}