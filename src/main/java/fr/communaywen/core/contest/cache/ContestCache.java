package fr.communaywen.core.contest.cache;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContestCache extends DatabaseConnector {

    private static ContestManager contestManager;
    private static JavaPlugin plugin;
    public ContestCache(AywenCraftPlugin plug, ContestManager manager) {
        contestManager = manager;
        plugin = plug;
    }

    private static final long cacheDuration = 120000;

    // CONTEST DATA
    private static ContestDataCache contestCache;

    public static void initContestDataCache() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "SELECT * FROM contest WHERE 1";
            try (PreparedStatement states = connection.prepareStatement(sql)) {
                ResultSet result = states.executeQuery();
                if (result.next()) {
                    String camp1 = result.getString("camp1");
                    String camp2 = result.getString("camp2");
                    String color1 = result.getString("color1");
                    String color2 = result.getString("color2");
                    int phase = result.getInt("phase");
                    String startdate = result.getString("startdate");

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        contestCache = new ContestDataCache(camp1, camp2, color1, color2, phase, startdate);
                    });
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String getCamp1Cache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getCamp1();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getCamp1();
            }
            return null;
        }
    }

    public static String getCamp2Cache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getCamp2();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getCamp2();
            }
            return null;
        }
    }

    public static String getColor1Cache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getColor1();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getColor1();
            }
            return null;
        }
    }

    public static String getColor2Cache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getColor2();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getColor2();
            }
            return null;
        }
    }

    public static int getPhaseCache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getPhase();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getPhase();
            }
            return -1;
        }
    }

    public static String getStartDateCache() {
        ContestDataCache cache = contestCache;

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getStartDate();
        } else {
            initContestDataCache();
            if (cache!=null) {
                return cache.getStartDate();
            }
            return null;
        }
    }

    // CONTEST PLAYER DATA
    private static final Map<UUID, ContestPlayerCache> playerCache = new HashMap<>();

    public static void initPlayerDataCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
            try (PreparedStatement states = connection.prepareStatement(sql)) {
                states.setString(1, playerUUID.toString());
                ResultSet result = states.executeQuery();
                if (result.next()) {
                    int points = result.getInt("point_dep");
                    int camp = result.getInt("camps");
                    String color = ContestManager.getString("contest","color" + camp).join();
                    ChatColor campColor = ChatColor.valueOf(color);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        playerCache.put(playerUUID, new ContestPlayerCache(points, camp, campColor));
                    });
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static int getPlayerPointsCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getPoints();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getPoints();
            }
            return 0;
        }
    }

    public static int getPlayerCampsCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getCamp();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getCamp();
            }
            return -1;
        }
    }
    public static ChatColor getPlayerColorCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getColor();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getColor();
            }
            return null;
        }
    }
}
