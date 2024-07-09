package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Insomnia implements Listener {
    public Set<Player> playersWhoSlept = new HashSet<>();
    public Random random;

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
            if (playersWhoSlept.isEmpty()) { return; }
            for (Player player : playersWhoSlept) {
                random = new Random();
                if (random.nextDouble() <= 0.1) {
                    System.out.println("Giving insomnia to "+player.getName());
                    player.sendMessage("Tu as fait une insomnie!");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1)); // Slowness II
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0)); // Weakness
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0)); // Blindness
                }
            }

            playersWhoSlept.clear();
        }
    }
}
