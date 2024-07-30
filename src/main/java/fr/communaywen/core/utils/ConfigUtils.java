package fr.communaywen.core.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigUtils {

    public static YamlConfiguration loadConfig(JavaPlugin plugin, String name) {
        File bookFile = new File(plugin.getDataFolder(), name);
        if (!bookFile.exists()) {
            plugin.saveResource(name, false);
        }
        return YamlConfiguration.loadConfiguration(bookFile);
    }

}
