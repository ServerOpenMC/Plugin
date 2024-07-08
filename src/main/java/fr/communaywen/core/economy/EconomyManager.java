package fr.communaywen.core.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
    private final Map<UUID, Double> balances;
    private final EconomyData economyData;

    public EconomyManager(File dataFolder) {
        this.economyData = new EconomyData(dataFolder);
        this.balances = economyData.loadBalances();
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void addBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        balances.put(uuid, getBalance(player) + amount);
        saveBalances();
    }

    public boolean withdrawBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double balance = getBalance(player);
        if (balance >= amount) {
            balances.put(uuid, balance - amount);
            saveBalances();
            return true;
        } else {
            return false;
        }
    }

    public boolean transferBalance(Player from, Player to, double amount) {
        if (withdrawBalance(from, amount)) {
            addBalance(to, amount);
            return true;
        } else {
            return false;
        }
    }

    private void saveBalances() {
        economyData.saveBalances(balances);
    }

    public Map<UUID, Double> getBalances() {
        return balances;
    }
}
