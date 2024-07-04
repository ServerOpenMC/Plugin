package fr.communaywen.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MOTDChanger {

    private final List<String> motdList = Arrays.asList(
            "Bienvenue sur notre serveur !",
            "Profitez de votre séjour !",
            "Découvrez de nouvelles aventures !",
            "Participez à nos événements spéciaux !",
            "Amusez-vous bien sur notre serveur !"
    );

    public void startMOTDChanger(JavaPlugin plugin) {
        AtomicInteger index = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                String nextMOTD = motdList.get(index.getAndIncrement() % motdList.size());
                Bukkit.getServer().setMotd(nextMOTD);
            }
        }.runTaskTimer(plugin, 0L, 12000L); // 12000 ticks = 10 minutes
    }
}
