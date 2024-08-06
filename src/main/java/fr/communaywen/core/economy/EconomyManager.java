package fr.communaywen.core.economy;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.quests.qenum.TYPE;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Feature("Economie")
@Credit("TheR0001")
public class EconomyManager {
    @Getter
    private final Map<UUID, Double> balances;

    public EconomyManager(File dataFolder) {
        this.balances = EconomyData.loadBalances();
    }

    public double getBalance(Player player, boolean sendMessage) {
        double balance = balances.getOrDefault(player.getUniqueId(), 0.0);

        if (sendMessage) {
            player.sendMessage(ChatColor.YELLOW + "Votre solde actuel est de " + ChatColor.GOLD + balance + " argents.");
        }

        return balance;
    }

    public void addBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        balances.put(uuid, getBalance(player, false) + amount);  // Utilisation de getBalance avec sendMessage=false

        saveBalances(player);
        for (QUESTS quests : QUESTS.values()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(player);
            if (quests.getType() == TYPE.MONEY) {
                if (!pq.isQuestCompleted(quests)) {
                    QuestsManager.manageQuestsPlayer(player, quests, (int) amount, " argents récoltés");
                }
            }
        }

        player.sendMessage(ChatColor.GREEN + "Vous avez reçu " + amount + " argents. Votre nouveau solde est de " + getBalance(player, false) + " argents.");
    }

    public boolean withdrawBalance(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        double balance = getBalance(player, false);  // Utilisation de getBalance avec sendMessage=false
        if (balance >= amount && amount > 0) {
            balances.put(uuid, balance - amount);
            saveBalances(player);
            for (QUESTS quests : QUESTS.values()) {
                PlayerQuests pq = QuestsManager.getPlayerQuests(player);
                if (quests.getType() == TYPE.MONEY) {
                    if (!pq.isQuestCompleted(quests)) {
                        pq.removeProgress(quests, (int) amount);
                    }
                }
            }

            player.sendMessage(ChatColor.GREEN + "Vous avez retiré " + amount + " argents. Votre nouveau solde est de " + getBalance(player, false) + " argents.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Échec du retrait de " + amount + " argents. Solde insuffisant ou montant invalide.");
            return false;
        }
    }

    public boolean transferBalance(Player from, Player to, double amount) {
        if (withdrawBalance(from, amount)) {
            addBalance(to, amount);
            from.sendMessage(ChatColor.GREEN + "Vous avez transféré " + amount + " argents à " + to.getName() + ". Votre nouveau solde est de " + getBalance(from, false) + " argents.");
            to.sendMessage(ChatColor.GREEN + "Vous avez reçu " + amount + " argents de " + from.getName() + ". Votre nouveau solde est de " + getBalance(to, false) + " argents.");
            return true;
        } else {
            from.sendMessage(ChatColor.RED + "Échec du transfert de " + amount + " argents à " + to.getName() + ". Solde insuffisant.");
            return false;
        }
    }

    private void saveBalances(Player player) {
        EconomyData.saveBalances(player, balances);
    }
}
