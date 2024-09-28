package fr.communaywen.core.homes;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;

public class HomeUpgradeManager {

    private final HomesManagers homesManagers;
    private final AywenCraftPlugin plugin;

    public HomeUpgradeManager(HomesManagers homesManagers, AywenCraftPlugin plugin) {
        this.homesManagers = homesManagers;
        this.plugin = plugin;
    }

    public HomeUpgrade getNextUpgrade(int currentHomes) {
        for (HomeUpgrade upgrade : HomeUpgrade.values()) {
            if (currentHomes < upgrade.getHomes()) {
                return upgrade;
            }
        }
        return null;
    }

    public void upgradeHomes(Player player) {
        int currentHomes = homesManagers.getHomeNamesByPlayer(player.getUniqueId()).size();
        int currentUpgrade = homesManagers.getCurrentHomesLimit(player.getUniqueId());
        HomeUpgrade nextUpgrade = getNextUpgrade(currentHomes);
    
        if (nextUpgrade != null) {
            double balance = plugin.getManagers().getEconomyManager().getBalance(player);
            int price = nextUpgrade.getPrice();
            HomesManagers homesManagers = plugin.getManagers().getHomesManagers();

            if (currentHomes < currentUpgrade) {
                MessageManager.sendMessageType(player, "§cVous n'avez pas atteint la limite de homes pour acheter cette amélioration.", Prefix.HOME, MessageType.ERROR, true);
                return;
            }
            
            if (balance >= price) {
                plugin.getManagers().getEconomyManager().withdrawBalance(player, price);
                int newHomesLimit = nextUpgrade.getHomes();
                homesManagers.upgradeHomesLimit(player.getUniqueId(), newHomesLimit);
    
                int updatedHomesLimit = homesManagers.getCurrentHomesLimit(player.getUniqueId());

                MessageManager.sendMessageType(player, "§aVous avez amélioré votre limite de homes à " + updatedHomesLimit + " pour " + nextUpgrade.getPrice() + "$.", Prefix.HOME, MessageType.SUCCESS, true);
            } else {
                MessageManager.sendMessageType(player, "§cVous n'avez pas assez d'argent pour améliorer votre limite de homes.", Prefix.HOME, MessageType.ERROR, true);
            }
        } else {
            MessageManager.sendMessageType(player, "§cVous avez atteint la limite maximale de homes.", Prefix.HOME, MessageType.ERROR, true);
        }
    }
}
