package fr.communaywen.core.levels;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import java.io.File;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.scoreboard.Criteria.DUMMY;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

       if (levelsFileConfig.contains("xp."+playerName+".amount")) {
           levelsFileConfig.set("xp."+playerName+".amount", levelsFileConfig.getInt("xp."+playerName+".amount") + amount);
       } else {
           levelsFileConfig.set("xp."+playerName+".amount", amount);
       }

        saveFile();
    }

    public static void removeToCount(Player player, int amount) {
        String playerName = player.getName();

        if (levelsFileConfig.contains("xp."+playerName+".amount")) {
            levelsFileConfig.set("xp."+playerName+".amount", levelsFileConfig.getInt("xp."+playerName+".amount") - amount);
        } else {
            levelsFileConfig.set("xp."+playerName+".amount", amount);
        }

        saveFile();
    }

    public static void setCount(Player player, int amount) {
        String playerName = player.getName();

        if (levelsFileConfig.contains("xp."+playerName+".amount")) {
            levelsFileConfig.set("xp."+playerName+".amount", amount);
        } else {
            levelsFileConfig.set("xp."+playerName+".amount", amount);
        }

        saveFile();
    }
}
