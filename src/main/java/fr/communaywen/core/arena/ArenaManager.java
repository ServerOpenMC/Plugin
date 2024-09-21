package fr.communaywen.core.arena;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ArenaManager extends DatabaseConnector {
    static FileConfiguration config;
    static JavaPlugin plugins;
    public ArenaManager(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;
    }

    public static void createArenaRegion(String world, String region, Location loc1, Location loc2) {

    }

    public static void tpPlayerOnRandomPosition(String arena, Player player) {
        List<Map<?, ?>> spawnpoints = config.getMapList("arena."+ arena +".spawnpoint");

        if (!spawnpoints.isEmpty()) {
            Random random = new Random();
            Map<?, ?> randomSpawn = spawnpoints.get(random.nextInt(spawnpoints.size()));

            double posX = (double) randomSpawn.get("posX");
            double posY = (double) randomSpawn.get("posY");
            double posZ = (double) randomSpawn.get("posZ");

            String worldName = config.getString("arena."+ arena +".world");

            if (Bukkit.getWorld(worldName) != null) {
                Location spawnLocation = new Location(Bukkit.getWorld(worldName), posX, posY, posZ);
                player.teleport(spawnLocation);

                MessageManager.sendMessageType(player, "§7Vous êtes arrivé dans une §cArene", Prefix.ARENA, MessageType.SUCCESS, true);
            }
        }
    }
}
