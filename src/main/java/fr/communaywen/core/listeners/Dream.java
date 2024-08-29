package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.World;
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

    World dreamworld;
    AywenCraftPlugin plugin;

    public Dream(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        dreamworld = plugin.getServer().getWorld("dreamworld");
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
                if (random.nextDouble() <= 0.4) { // Pourcentage de chance d
                    Random r = new Random();
                    int range = 1000;
                    int x = r.nextInt(range - (range * -1)) + (range * -1);
                    int y = r.nextInt(range - (range * -1)) + (range * -1);
                    player.teleport(dreamworld.getHighestBlockAt(x, y).getLocation().add(0,1,0));
                }
            }

            playersWhoSlept.clear();
        }
    }
}
