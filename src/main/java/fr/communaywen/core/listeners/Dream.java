package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Dream implements Listener {
    public Set<Player> playersWhoSlept = new HashSet<>();
    public Random random;

    AywenCraftPlugin plugin;

    public Dream(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            playersWhoSlept.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        playersWhoSlept.remove(event.getPlayer());
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            if (playersWhoSlept.isEmpty()) {
                return;
            }
            for (Player player : playersWhoSlept) {
                random = new Random();
                if (random.nextDouble() <= 0.1) {
                    this.plugin.getManagers().getDreamdimManager().getUtils().joinDimension(player);
                }
            }

            playersWhoSlept.clear();
        }
    }
}
