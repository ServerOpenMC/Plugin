package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.communaywen.core.managers.LeaderboardManager.updateLeaderboardBalTop;
import static fr.communaywen.core.managers.LeaderboardManager.updateLeaderboardTeamTop;


public class LeaderboardListener implements Listener {
    private BukkitRunnable eventRunnable;

    public LeaderboardListener(AywenCraftPlugin plugin) {
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() >= 1) {
                    updateLeaderboardBalTop();
                    updateLeaderboardTeamTop();
                }
            }
        };
        eventRunnable.runTaskTimer(plugin, 0, 5 * 60 * 20L);
    }
}