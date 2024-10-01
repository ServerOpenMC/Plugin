package fr.communaywen.core.contest;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FirerocketSpawnListener implements Listener {

    private BukkitRunnable eventRunnable;

    static FileConfiguration config;
    static JavaPlugin plugins;
    public FirerocketSpawnListener(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;

        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(ContestManager.getPhaseCache() != 1 && Bukkit.getOnlinePlayers().size() >= 1) {
                    spawnFireworksInWorldEditRegion();
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

                int x = random.nextInt((max.getBlockX() - min.getBlockX()) + 1) + min.getBlockX();
                int z = random.nextInt((max.getBlockZ() - min.getBlockZ()) + 1) + min.getBlockZ();
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

        String camp1Color = ContestManager.getColor1Cache();
        String camp2Color = ContestManager.getColor2Cache();
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
