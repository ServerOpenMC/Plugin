package fr.communaywen.core.clockinfos.tasks;

import fr.communaywen.core.credit.Credit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Credit("Fnafgameur")
public class CompassClockTask extends BukkitRunnable {

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            Material itemInHand = containsCompassOrClock(player);

            if (itemInHand == null) {
                continue;
            }

            if (itemInHand.equals(Material.CLOCK)) {

                String worldName = player.getWorld().getName();

                // Get the number of days passed since the beginning of the world
                long numberOfDaysPassed = player.getWorld().getFullTime() / 24000;

                // Get the current time
                long currentTime = player.getWorld().getTime();
                long time = currentTime + (numberOfDaysPassed * 24000);
                long hours = (time / 1000 + 6) % 24;
                long minutes = (time % 1000) * 60 / 1000;
                String minutesString = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);

                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent("§6Jour: §e" + numberOfDaysPassed + " §7| " + "§6Heure: §e" + hours + "h" + minutesString + " §7| " + "§6Monde: §e" + worldName)
                );

                continue;
            }

            // Get player's current location
            Location playerLocation = player.getLocation();
            int x = playerLocation.getBlockX();
            int y = playerLocation.getBlockY();
            int z = playerLocation.getBlockZ();

            // Get world's spawn location
            Location spawnLocation = player.getWorld().getSpawnLocation();
            int spawnX = spawnLocation.getBlockX();
            int spawnY = spawnLocation.getBlockY();
            int spawnZ = spawnLocation.getBlockZ();

            // Get player's respawn location
            Location respawnLocation = player.getRespawnLocation();
            if (respawnLocation == null) {
                respawnLocation = player.getWorld().getSpawnLocation();
            }
            int respawnX = respawnLocation.getBlockX();
            int respawnY = respawnLocation.getBlockY();
            int respawnZ = respawnLocation.getBlockZ();

            // Get player's direction
            int yaw = (int) playerLocation.getYaw();
            String directionFacing = getDirectionFacing(yaw);

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(
                            "§6Position: §e" + x + " " + y + " " + z + " §7| " +
                            "§6Spawn: §e" + spawnX + " " + spawnY + " " + spawnZ + " §7| " +
                            "§6Respawn: §e" + respawnX + " " + respawnY + " " + respawnZ + " §7| " +
                            "§6Direction: §e" + directionFacing
                    )
            );

        }
    }

    /**
     * Check if the player is holding a compass or a clock in their main hand or offhand
     * @param player The player to check
     * @return The material of the item if the player is holding a compass or a clock, null otherwise
     */
    private Material containsCompassOrClock(Player player) {
        Material itemInMainHand = player.getInventory().getItemInMainHand().getType();

        if (itemInMainHand.equals(Material.COMPASS) || itemInMainHand.equals(Material.CLOCK)) {
            return itemInMainHand;
        }

        Material itemInOffHand = player.getInventory().getItemInOffHand().getType();

        if (itemInOffHand.equals(Material.COMPASS) || itemInOffHand.equals(Material.CLOCK)) {
            return itemInOffHand;
        }

        return null;
    }

    /**
     * Get the direction the player is facing
     * @param yaw The yaw of the player
     * @return The direction the player is facing (North, East, South or West)
     */
    private String getDirectionFacing(int yaw) {

        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 315 || yaw < 45) {
            return "Sud (+Z)";
        }
        if (yaw < 135) {
            return "Ouest (-X)";
        }
        if (yaw < 225) {
            return "Nord (-Z)";
        }

        return "Est (+X)";
    }
}
