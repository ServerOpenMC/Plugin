package fr.communaywen.core.personalhome;


import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class HomesUtils {
    public static int[] deserializeCoords(String coords) {
        String[] parts = coords.split(";");

        if (parts.length == 3) {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            return new int[]{x, y, z};
        } else if (parts.length == 5) {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            int yaw = Integer.parseInt(parts[3]);
            int pitch = Integer.parseInt(parts[4]);

            return new int[]{x, y, z, yaw, pitch};
        } else {
            return null;
        }
    }

    public static String serializeCoords(Location location) {
        return location.getBlockX()+";"+
               location.getBlockY()+";"+
               location.getBlockZ()+";"+
               Math.round(location.getYaw())+";"+
               Math.round(location.getPitch());
    }

    public static boolean isOnPlatform(Location location, int platformId) {
        double x = location.getX();
        double z = location.getZ();

        int platformStartX = platformId * 13 * 16;
        int platformEndX = platformStartX + 32;

        return x >= platformStartX && x < platformEndX && z >= 0 && z < 32;
    }

    public static boolean isOutOfHome(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();

        if (z < 0 || z >= 32) {
            return true;
        }

        int relativeX = Math.abs(x) % (208); // 13*16

        return relativeX >= 32;
    }

    public static boolean isLocationOnPlatform(int x, int z, int platformId) {
        int platformStartX = platformId * 13 * 16;
        int platformEndX = platformStartX + 32;

        boolean isXInRange = x >= platformStartX && x < platformEndX;
        boolean isZInRange = z >= 0 && z < 32;

        return isXInRange && isZInRange;
    }

    public static Integer getPlatformIdFromCoordinates(int x, int z) {
        if (z >= 0 && z < 32) {
            int gridX = Math.floorDiv(x / 16, 13);
            int localX = (x / 16) - gridX * 13;

            if (localX >= 0 && localX < 2) {
                return gridX+1;
            }
        }

        return null;
    }

    public static boolean isntInHisHome(Player player, Location location) {
        HashMap<UUID, Home> homes = AywenCraftPlugin.getInstance().getManagers().getHomeManager().getHomes();

        Integer id = getPlatformIdFromCoordinates(location.getBlockX(), location.getBlockZ());
        if (id == null) { return true; }

        return id != homes.get(player.getUniqueId()).getId();
    }
}
