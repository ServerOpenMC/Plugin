package fr.communaywen.core.economy;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
    private final Map<UUID, Double> balances = new HashMap<>();

    public EconomyManager(File dataFolder) {
        // Initialisation du gestionnaire d'économie, chargement des données depuis le fichier, etc.
    }

    public void addBalance(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        balances.put(playerId, balances.getOrDefault(playerId, 0.0) + amount);
        // Sauvegarder les données si nécessaire
    }

    public void withdraw(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        balances.put(playerId, balances.getOrDefault(playerId, 0.0) - amount);
        // Sauvegarder les données si nécessaire
    }

    public void deposit(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        balances.put(playerId, balances.getOrDefault(playerId, 0.0) + amount);
        // Sauvegarder les données si nécessaire
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }
}
