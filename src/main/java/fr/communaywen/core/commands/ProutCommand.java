package fr.communaywen.core.commands;

import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * THE Prout command.
 *
 * Usage: /prout
 * Permission: PREFIX.command.prout
 */
public final class ProutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final @NotNull CommandSender sender,
                             final @NotNull Command command,
                             final @NotNull String args,
                             final @NotNull String[] strings) {
        if (sender instanceof Player player) {
            player.sendMessage("§2Beuuurk, ça pue !");

            // Make the player jump
            final Vector currentVelocity = player.getVelocity();
            currentVelocity.setY(0.55d);

            player.setVelocity(currentVelocity);

            // Spawn some cloud particles
            final Location location = player.getLocation();
            final @Nullable World world = location.getWorld();

            if (world != null) {
                world.spawnParticle(org.bukkit.Particle.CLOUD, location, 3, 0.02d, -0.04d, 0.02d, 0.09d);

                // Funny sound!
                world.playSound(location, org.bukkit.Sound.ENTITY_VILLAGER_NO, 0.8f, 2.3f);
                world.playSound(location, Sound.ENTITY_GOAT_EAT, 0.7f, 0.2f);
            }
        }

        return true;
    }
}
