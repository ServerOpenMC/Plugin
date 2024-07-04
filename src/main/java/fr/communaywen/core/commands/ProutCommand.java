package fr.communaywen.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * THE Prout command.
 *
 * Usage: /prout
 * Permission: PREFIX.command.prout
 */
public final class ProutCommand implements CommandExecutor {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_TIME = 300; // 5 minutes in seconds

    @Override
    public boolean onCommand(final @NotNull CommandSender sender,
                             final @NotNull Command command,
                             final @NotNull String label,
                             final @NotNull String[] args) {
        if (sender instanceof Player player) {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis() / 1000;

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = currentTime - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                    return true;
                }
            }

            player.sendMessage("§2Beuuurk, ça pue !");

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

            // Add glowing effect for 30 seconds
            addGlowingEffect(player);

            // Broadcast the message
            String broadcastMessage = "[&c&l&ka&r] &f&lPROUT !!! &r" + player.getName() + " a &f&lpété&r. &2&lBeurk !";
            Bukkit.broadcastMessage(broadcastMessage);

            // Update cooldown
            cooldowns.put(playerId, currentTime);
        }

        return true;
    }

    private void addGlowingEffect(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("glowGreen");

        if (team == null) {
            team = scoreboard.registerNewTeam("glowGreen");
            team.setColor(org.bukkit.ChatColor.GREEN);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        team.addEntry(player.getName());
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 0, false, false, true));

        Bukkit.getScheduler().runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("AywenCraftPlugin"), () -> team.removeEntry(player.getName()), 600L);
    }
}
