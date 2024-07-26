package fr.communaywen.core.economy;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.quests.qenum.TYPE;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Feature("Economie")
@Credit("TheR0001")
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
        for(QUESTS quests : QUESTS.values()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(player);
            if(quests.getType() == TYPE.MONEY) {
                if(!pq.isQuestCompleted(quests)) {
                    AywenCraftPlugin.getInstance().getLogger().info(String.valueOf(pq.getProgress(quests)));
                    AywenCraftPlugin.getInstance().getLogger().info(String.valueOf(getBalance(player)));
                    QuestsManager.manageQuestsPlayer(player, quests, (int) amount, " argents récoltés");
                }
            }
        }
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
