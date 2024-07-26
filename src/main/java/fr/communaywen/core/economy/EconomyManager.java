package fr.communaywen.core.economy;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Feature("Economie")
@Credit("TheR0001")
public class EconomyManager {
    @Getter
    private final Map<UUID, Double> balances;
    private final EconomyData economyData;

    public EconomyManager(File dataFolder) {
        this.economyData = new EconomyData(dataFolder);
        this.balances = economyData.loadBalances();
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    public void addBalance(UUID player, double amount) {
        balances.put(player, getBalance(player) + amount);
        saveBalances();
    }

    public boolean withdrawBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double balance = getBalance(player.getUniqueId());
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
            addBalance(to.getUniqueId(), amount);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasEnoughBalance(Player player, double amount) {
        return getBalance(player.getUniqueId()) >= amount;
    }

    public static String formatValue(double value) {
        return value > 0 ? ChatColor.GREEN + String.valueOf(value) : ChatColor.RED + String.valueOf(value);
    }

    private void saveBalances() {
        economyData.saveBalances(balances);
    }

}
