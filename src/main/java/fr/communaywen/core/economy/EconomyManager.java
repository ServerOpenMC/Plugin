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
import org.bukkit.Bukkit;
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
@Credit({"TheR0001", "Axeno"})
public class EconomyManager {
    @Getter private static Map<UUID, Double> balances = Map.of();
    @Getter static EconomyManager instance;

    public EconomyManager() {
        balances = EconomyData.loadBalances();
        instance = this;
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    public void addBalance(UUID player, double amount) {
        balances.put(player, getBalance(player) + amount);

        saveBalances(player);
        if (Bukkit.getPlayer(player).isOnline()) {
            for (QUESTS quests : QUESTS.values()) {
                PlayerQuests pq = QuestsManager.getPlayerQuests(player);
                if (quests.getType() == TYPE.MONEY) {
                    if (!pq.isQuestCompleted(quests)) {
                        QuestsManager.manageQuestsPlayer(player, quests, (int) amount, " argents récoltés");
                    }
                }
            }
        }
    }

    public void addBalance(UUID uuid, double amount, String reason) {
        balances.put(uuid, getBalance(uuid) + amount);

        new TransactionsManager().addTransaction(new Transaction(
                uuid.toString(),
                "CONSOLE",
                amount,
                reason
        ));

        saveBalances(uuid);
        for(QUESTS quests : QUESTS.values()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(uuid);
            if(quests.getType() == TYPE.MONEY) {
                if(!pq.isQuestCompleted(quests)) {
                    QuestsManager.manageQuestsPlayer(uuid, quests, (int) amount, " argents récoltés");
                }
            }
        }
    }

    public boolean withdrawBalance(UUID uuid, double amount) {
        double balance = getBalance(uuid);
        if (balance >= amount && amount > 0) {
            balances.put(uuid, balance - amount);
            saveBalances(uuid);
            for(QUESTS quests : QUESTS.values()) {
                PlayerQuests pq = QuestsManager.getPlayerQuests(uuid);
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

    public boolean transferBalance(UUID from, UUID to, double amount) {
        if (withdrawBalance(from, amount)) {
            addBalance(to, amount);
            return true;
        } else {
            return false;
        }
    }

    private static void saveBalances(UUID player) {
        EconomyData.saveBalances(player, balances);
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
