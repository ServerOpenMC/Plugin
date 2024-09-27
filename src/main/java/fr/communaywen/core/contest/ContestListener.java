package fr.communaywen.core.contest;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ContestListener implements Listener {
    private BukkitRunnable eventRunnable;

    public ContestListener(AywenCraftPlugin plugin, FileConfiguration eventConfig) {
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestManager.getStartDateCache()));
                int phase = ContestManager.getPhaseCache();

                if (phase == 1 && ContestManager.getCurrentDayOfWeek().getValue() == dayStartContestOfWeek.getValue()) {
                    ContestManager.initPhase1();
                }
                int dayStart = dayStartContestOfWeek.getValue() + 1;
                if (dayStart==8) {dayStart=1;}
                if (phase == 2 && ContestManager.getCurrentDayOfWeek().getValue() == dayStart) {
                    ContestManager.initPhase2(plugin, eventConfig);
                }
                int dayEnd = dayStart + 2;
                if (dayEnd>=8) {
                    dayEnd=1;
                } //attention ne pas modifier les valeurs de départ des contest sinon le systeme va broke
                if (phase == 3 && ContestManager.getCurrentDayOfWeek().getValue() == dayEnd) {
                    ContestManager.initPhase3(plugin, eventConfig);
                }
            }
        };
        // tout les minutes
        eventRunnable.runTaskTimer(plugin, 0, 1200);
     };
}
