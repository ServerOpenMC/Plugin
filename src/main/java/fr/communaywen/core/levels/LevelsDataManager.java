package fr.communaywen.core.levels;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static org.bukkit.Bukkit.getLogger;


public class LevelsDataManager {

    private static FileConfiguration levelsFileConfig;
    private static File levelsFile;

    public static void setLevelsFile(FileConfiguration levelsFileConfigs, File levelsFiles) {
        levelsFileConfig = levelsFileConfigs;
        levelsFile = levelsFiles;
    }

    public static void saveFile() {
        try {
            levelsFileConfig.save(levelsFile);
        } catch (IOException e) {
            getLogger().severe("Error saving levels.yml");
            throw new RuntimeException(e);
        }
    }

    public static void addToCount(Player player, int amount) {
        String playerName = player.getName();

        if (levelsFileConfig.contains("xp." + playerName + ".amount")) {
            levelsFileConfig.set("xp." + playerName + ".amount", levelsFileConfig.getInt("xp." + playerName + ".amount") + amount);
        } else {
            levelsFileConfig.set("xp." + playerName + ".amount", amount);
        }

        saveFile();
    }

    public static void removeToCount(Player player, int amount) {
        String playerName = player.getName();

        if (levelsFileConfig.contains("xp." + playerName + ".amount")) {
            levelsFileConfig.set("xp." + playerName + ".amount", levelsFileConfig.getInt("xp." + playerName + ".amount") - amount);
        } else {
            levelsFileConfig.set("xp." + playerName + ".amount", amount);
        }

        saveFile();
    }

    public static void setCount(Player player, int amount) {
        String playerName = player.getName();

        levelsFileConfig.set("xp." + playerName + ".amount", amount);

        saveFile();
    }

    public static int getCount(Player player) {
        String playerName = player.getName();

        if (levelsFileConfig.contains("xp." + playerName + ".amount")) {
            return levelsFileConfig.getInt("xp." + playerName + ".amount");
        } else {
            return 0;
        }
    }
}
