package fr.communaywen.core.contest.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.managers.RegionsManager;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.contest.managers.ColorConvertor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FirerocketSpawnListener implements Listener {

    private BukkitRunnable eventRunnable;

    FileConfiguration config;
    JavaPlugin plugins;
    private final ContestManager contestManager;

    public FirerocketSpawnListener(AywenCraftPlugin plugin, ContestManager manager) {
        this.contestManager = manager;
        config = plugin.getConfig();
        plugins = plugin;

        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (RegionsManager.isPlayerInRegion((String) config.get("contest.config.spawnRegionName"), Bukkit.getWorld((String) config.get("contest.config.worldName"))) && Bukkit.getOnlinePlayers().size() >= 1) {
                    if (ContestCache.getPhaseCache() != 1) {
                        spawnFireworksInWorldEditRegion();
                    }
                }
            }
        };
        eventRunnable.runTaskTimer(plugin, 0, 400);
    }

    private void spawnFireworksInWorldEditRegion() {
        String worldsName = (String) config.get("contest.config.worldName");
        String regionsName = (String) config.get("contest.config.spawnRegionName");

        try {
            org.bukkit.World world = Bukkit.getWorld(worldsName);
            com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

            RegionManager regions = container.get(wgWorld);
            ProtectedRegion region = regions.getRegion(regionsName);

            for (int i = 0; i<6; i++) {
                BlockVector3 min = region.getMinimumPoint();
                BlockVector3 max = region.getMaximumPoint();

                Random random = new Random();

                int x = random.nextInt((max.x() - min.x()) + 1) + min.x();
                int z = random.nextInt((max.z() - min.z()) + 1) + min.z();
                int y = 81;

                Location location = new Location(world, x, y, z);
                spawnFireworkAtLocation(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spawnFireworkAtLocation(Location location) {

        Firework firework = (Firework) location.getWorld().spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        String camp1Color = ContestCache.getColor1Cache();
        String camp2Color = ContestCache.getColor2Cache();
        ChatColor color1 = ChatColor.valueOf(camp1Color);
        ChatColor color2 = ChatColor.valueOf(camp2Color);

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(ColorConvertor.getFirerocketColorFromChatColor(color1))
                .withFade(ColorConvertor.getFirerocketColorFromChatColor(color2))
                .with(FireworkEffect.Type.BALL)
                .withFlicker()
                .build();


        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);

        new BukkitRunnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(plugins, 50);


    }
}
