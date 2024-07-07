package fr.communaywen.core.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private final Map<String, Double> balances = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public EconomyManager(File dataFolder) {
        configFile = new File(dataFolder, "economy.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadBalances();
    }

    private void loadBalances() {
        for (String key : config.getKeys(false)) {
            balances.put(key, config.getDouble(key));
        }
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getName(), 0.0);
    }

    public void setBalance(Player player, double amount) {
        balances.put(player.getName(), amount);
        config.set(player.getName(), amount);
        saveConfig();
    }

    public void withdraw(Player player, double amount) {
        double currentBalance = getBalance(player);
        if (currentBalance >= amount) {
            setBalance(player, currentBalance - amount);
        } else {
            // Gérer le cas où le joueur n'a pas suffisamment de fonds
            // Par exemple, lancer une exception ou enregistrer un message
        }
    }

    public void deposit(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
