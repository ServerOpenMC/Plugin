package fr.communaywen.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MOTDChanger extends JavaPlugin {

    private final List<String> motdList = Arrays.asList(
            "Premier MOTD",
            "Second MOTD",
            "Troisième MOTD",
            "Quatrième MOTD",
            "Cinquième MOTD"
    );

    @Override
    public void onEnable() {
        startMOTDChanger();
    }

    private void startMOTDChanger() {
        AtomicInteger index = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                String nextMOTD = motdList.get(index.getAndIncrement() % motdList.size());
                Bukkit.getServer().setMotd(nextMOTD);
            }
        }.runTaskTimer(this, 0L, 12000L); // 12000 ticks = 10 minutes
    }

    @Override
    public void onDisable() {
    }
}
