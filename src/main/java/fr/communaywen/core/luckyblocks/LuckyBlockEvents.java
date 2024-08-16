package fr.communaywen.core.luckyblocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class LuckyBlockEvents implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.GOLD_BLOCK) return;

        Location location = e.getBlock().getLocation().add(0.5, 0, 0.5);
        e.setCancelled(true);
        e.getBlock().setType(Material.AIR);

        int randomNumber = random.nextInt(3);
        switch (randomNumber) {
            case 0:
                location.getWorld().spawn(location, SkeletonHorse.class);
                break;

            case 1:
                location.getWorld().spawn(location, ZombieHorse.class);
                break;

            case 2:
                location.getWorld().createExplosion(location, 2, true);
                break;
        }
    }
}
