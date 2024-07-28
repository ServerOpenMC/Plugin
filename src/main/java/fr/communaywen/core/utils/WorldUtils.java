package fr.communaywen.core.utils;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class WorldUtils {

    public static Yaw getYaw(Player p) {
        float yaw = p.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360; // true modulo, as javas modulo is weird for negative values
        if (yaw > 135 || yaw < -135) {
            return Yaw.NORTH;
        } else if (yaw < -45) {
            return Yaw.EAST;
        } else if (yaw > 45) {
            return Yaw.WEST;
        } else {
            return Yaw.SOUTH;
        }
    }

}
