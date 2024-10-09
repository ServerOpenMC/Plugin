package fr.communaywen.core.contest.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.contest.managers.ContestManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ContestListener implements Listener {
    private BukkitRunnable eventRunnable;
    private final ContestManager contestManager;

    public ContestListener(AywenCraftPlugin plugin, FileConfiguration eventConfig, ContestManager manager) {
        this.contestManager = manager;
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestCache.getStartDateCache()));
                int phase = ContestCache.getPhaseCache();

                if (phase == 1 && contestManager.getCurrentDayOfWeek().getValue() == dayStartContestOfWeek.getValue()) {
                    contestManager.initPhase1();
                }
                int dayStart = dayStartContestOfWeek.getValue() + 1;
                if (dayStart==8) {dayStart=1;}
                if (phase == 2 && contestManager.getCurrentDayOfWeek().getValue() == dayStart) {
                    contestManager.initPhase2(plugin, eventConfig);
                }
                int dayEnd = dayStart + 2;
                if (dayEnd>=8) {
                    dayEnd=1;
                } //attention ne pas modifier les valeurs de départ des contest sinon le systeme va broke
                if (phase == 3 && contestManager.getCurrentDayOfWeek().getValue() == dayEnd) {
                    contestManager.initPhase3(plugin, eventConfig);
                }
            }
        };
        // tout les minutes
        eventRunnable.runTaskTimer(plugin, 0, 1200);
     };
}
