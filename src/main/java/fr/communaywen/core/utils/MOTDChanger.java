package fr.communaywen.core.utils;

import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Feature("MOTD")
@Credit("Elone")
@Collaborators("Margouta")
public class MOTDChanger {

    private final List<String> motdList = Arrays.asList(
        "OpenMC - C'est VOTRE serveur SMP en 1.21",
        "OpenMC - Serveur SMP 1.21 - Rejoignez-nous",
        "OpenMC - Explorez votre monde en 1.21",
        "OpenMC - Votre aventure SMP commence ici en 1.21",
        "OpenMC - La communauté en 1.21 vous attend",
        "OpenMC - Créé par les joueurs, pour les joueurs en 1.21",
        "OpenMC - Découvrez les nouveautés de la 1.21",
        "OpenMC - Vivez l'expérience SMP ultime en 1.21",
        "OpenMC - Construisez, explorez, survivez en 1.21",
        "OpenMC - Rejoignez notre serveur SMP en 1.21"
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
