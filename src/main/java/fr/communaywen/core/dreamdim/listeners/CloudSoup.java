package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.dreamdim.AdvancementRegister;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CloudSoup implements Listener {
    HashMap<UUID, Integer> cooldown = new HashMap<>();
    AywenCraftPlugin plugin;
    AdvancementRegister register;

    @Getter
    private static CloudSoup instance;

    public CloudSoup(AywenCraftPlugin plugin, AdvancementRegister register) {
        instance = this;
        this.plugin = plugin;
        this.register = register;
    }

    @EventHandler
    public void onPlayerEnter(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) > 0) {
            player.setAllowFlight(true);
        }
    }

    public String getFlyTime(Player player) {
        if (cooldown.containsKey(player.getUniqueId())) {
            int minutes_timeleft = cooldown.get(player.getUniqueId()) / 60;
            int seconds_timeleft = cooldown.get(player.getUniqueId()) % 60;
            return minutes_timeleft+"min "+seconds_timeleft+"sec";
        }
        return null;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        CustomStack customStack = CustomStack.byItemStack(event.getItem());
        Player player = event.getPlayer();
        UUID playeruuid = player.getUniqueId();

        if (customStack == null) { return; }
        if (customStack.getNamespacedID().equals("aywen:cloud_soup")) {
            register.grantAdvancement(player, "aywen:leave_earth");

            if (cooldown.containsKey(player.getUniqueId())) {
                cooldown.put(playeruuid, cooldown.get(playeruuid) + 300);
                int minutes_timeleft = cooldown.get(playeruuid) / 60;
                int seconds_timeleft = cooldown.get(playeruuid) % 60;
                player.sendMessage("§aVous pouvez voler pendant 5 minutes de plus ("+minutes_timeleft+"min "+seconds_timeleft+"sec)");
            } else {
                player.sendMessage("§aVous pouvez voler pendant 5 minutes.");
                startTimer(player);
            }
        }
    }

    public void close() {
        for (UUID playeruuid : cooldown.keySet()) {
            plugin.getServer().getOfflinePlayer(playeruuid).setAllowFlight(false);
        }
    }

    public void startTimer(Player player) {
        UUID playeruuid = player.getUniqueId();
        cooldown.put(playeruuid, 300); //300s = 5 minutes
        player.setAllowFlight(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (cooldown.containsKey(playeruuid) && cooldown.get(playeruuid) > 0) {
                        player.setAllowFlight(true);
                        cooldown.put(playeruuid, cooldown.get(playeruuid) - 1);

                        if (cooldown.get(playeruuid) == 60) {
                            player.sendMessage("§aIl vous reste 1 minute de vol");
                        } else if (cooldown.get(playeruuid) == 30) {
                            player.sendMessage("§aIl vous reste 30 secondes de vol");
                        } else if (cooldown.get(playeruuid) == 10) {
                            player.sendMessage("§aIl vous reste 10 secondes de vol");
                        } else if (cooldown.get(playeruuid) == 3) {
                            player.sendMessage("§aIl vous reste 3 secondes de vol");
                        } else if (cooldown.get(playeruuid) == 2) {
                            player.sendMessage("§aIl vous reste 2 secondes de vol");
                        } else if (cooldown.get(playeruuid) == 1) {
                            player.sendMessage("§aIl vous reste 1 secondes de vol");
                        }
                    } else {
                        player.sendMessage("§cVotre soupe de nuage s'est épuisée");
                        cooldown.remove(playeruuid);
                        player.setAllowFlight(false);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }
}
