package fr.communaywen.core.space.moon;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class Utils {

    // Alors oui, c'est fait avec Claude mais j'étais trop éclaté en maths pour le faire tout seul - ri1_
    static public boolean isPlayerLookingAtMoon(Player player) {
        World world = player.getWorld();

        // Check if it's night (time is between 13000 and 23000)
        long time = world.getTime();
        if (time < 13000 || time > 23000) {
            return false;
        }

        // Get the player's view direction
        Vector playerDirection = player.getLocation().getDirection().normalize();

        // Calculate the moon's position (this is an approximation)
        double moonAngle = (time - 13000) / 10000.0 * Math.PI;
        Vector moonDirection = new Vector(Math.cos(moonAngle), Math.sin(moonAngle), 0).normalize();

        // Compare the player's view direction with the moon's position
        double dotProduct = playerDirection.dot(moonDirection);

        // If the dot product is close to 1, the player is looking at the moon
        return dotProduct > 0.98; // You can adjust this threshold as needed
    }

}
