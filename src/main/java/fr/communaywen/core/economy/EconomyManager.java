package fr.communaywen.core.economy;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.quests.qenum.TYPE;
import fr.communaywen.core.utils.Transaction;
import fr.communaywen.core.utils.database.TransactionsManager;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Feature("Economie")
@Credit("TheR0001")
public class EconomyManager {
    @Getter
    private static Map<UUID, Double> balances = Map.of();

    @Getter static EconomyManager instance;

    public EconomyManager(File dataFolder) {
        balances = EconomyData.loadBalances();
        instance = this;
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    public static double getBalanceOffline(OfflinePlayer player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }


    public void addBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        balances.put(uuid, getBalance(player.getUniqueId()) + amount);

        saveBalances(player);
        for(QUESTS quests : QUESTS.values()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(player.getUniqueId());
            if(quests.getType() == TYPE.MONEY) {
                if(!pq.isQuestCompleted(quests)) {
                    QuestsManager.manageQuestsPlayer(player, quests, (int) amount, " argents récoltés");
                }
            }
        }
    }

    public void addBalance(Player player, double amount, String reason) {
        UUID uuid = player.getUniqueId();
        balances.put(uuid, getBalance(player.getUniqueId()) + amount);

        new TransactionsManager().addTransaction(new Transaction(
                player.getUniqueId().toString(),
                "CONSOLE",
                amount,
                reason
        ));

        saveBalances(player);
        for(QUESTS quests : QUESTS.values()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(player.getUniqueId());
            if(quests.getType() == TYPE.MONEY) {
                if(!pq.isQuestCompleted(quests)) {
                    QuestsManager.manageQuestsPlayer(player, quests, (int) amount, " argents récoltés");
                }
            }
        }
    }

    public static void addBalanceOffline(OfflinePlayer player, double amount) {
        UUID uuid = player.getUniqueId();
        balances.put(uuid, getBalanceOffline(player) + amount);

        saveBalancesOffline(player);
    }

    public boolean withdrawBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double balance = getBalance(player.getUniqueId());
        if (balance >= amount && amount > 0) {
            balances.put(uuid, balance - amount);
            saveBalances(player);
            for(QUESTS quests : QUESTS.values()) {
                PlayerQuests pq = QuestsManager.getPlayerQuests(player.getUniqueId());
                if(quests.getType() == TYPE.MONEY) {
                    if(!pq.isQuestCompleted(quests)) {
                        pq.removeProgress(quests, (int) amount);
                    }
                }
            }
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

    private void saveBalances(Player player) {
        EconomyData.saveBalances(player, balances);
    }
    private static void saveBalancesOffline(OfflinePlayer player) {
        EconomyData.saveBalancesOffline(player, balances);
    }

    public String getFormattedBalance(UUID player) {
        String balance = String.valueOf(getBalance(player));
        Currency currency = Currency.getInstance("EUR");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        formatter.setCurrency(currency);
        BigDecimal bd = new BigDecimal(balance);
        return formatter.format(bd);
    }
}
