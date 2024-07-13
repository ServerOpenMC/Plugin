package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PasFraisListener implements Listener {
    AywenCraftPlugin plugin;

    public PasFraisListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isFresh(String namespace) {
        return switch (namespace) {
            case "aywen:kebab" -> true;
            case "porkchop" -> true;
            case "mutton" -> true;
            case "chicken" -> true;
            case "beef" -> true;
            case "cod" -> true;
            case "salmon" -> true;
            case "tropical_fish" -> true;
            default -> false;
        };
    }

    private class RepeatedProut extends BukkitRunnable {
        private int count = 0;
        Player player;

        public RepeatedProut(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            if (count < 5) {
                count++;
                // Make the player jump
                final Vector currentVelocity = player.getVelocity();
                currentVelocity.setY(0.55d);

                player.setVelocity(currentVelocity);

                // Spawn some cloud particles
                final Location location = player.getLocation();
                final @Nullable World world = location.getWorld();

                if (world != null) {
                    world.spawnParticle(Particle.CLOUD, location, 3, 0.02d, -0.04d, 0.02d, 0.09d);

                    // Funny sound!
                    world.playSound(location, Sound.ENTITY_VILLAGER_NO, 0.8f, 2.3f);
                    world.playSound(location, Sound.ENTITY_GOAT_EAT, 0.7f, 0.2f);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        String name;
        CustomStack customstack = CustomStack.byItemStack(item);

        try {
            name = customstack.getNamespacedID();
        } catch (Exception e) {
            name = item.getType().name().toLowerCase();
        }

        Random random = new Random();
        if ((isFresh(name)) && (random.nextDouble() <= 0.1)) {
            player.sendMessage("§2Beuuurk, c'était pas frais !");
            new RepeatedProut(player).runTaskTimer(this.plugin, 0L, (new Random().nextInt(10) + 1) * 20L);
        }
    }
}
