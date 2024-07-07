package fr.communaywen.core.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyData {
    private final File file;
    private final FileConfiguration config;

    public EconomyData(File dataFolder) {
        file = new File(dataFolder, "economy.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveBalances(Map<UUID, Double> balances) {
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Double> loadBalances() {
        Map<UUID, Double> balances = new HashMap<>();
        for (String key : config.getKeys(false)) {
            balances.put(UUID.fromString(key), config.getDouble(key));
        }
        return balances;
    }
}
