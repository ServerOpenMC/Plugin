package fr.communaywen.core.commands.fun;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

@Feature("Rewards")
@Credit("Gyro3630")
public class RewardCommand extends DatabaseConnector {
    AywenCraftPlugin plugin;

    public RewardCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        connection = plugin.getManagers().getDatabaseManager().getConnection();
    }

    public boolean hasClaimReward(Player player, String scope) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT isClaimed FROM events_rewards WHERE player = ? AND scope = ?");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, scope);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            player.sendMessage("§cUne erreur est survenue");
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public void claim(Player player, String scope) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events_rewards VALUES (?, ?, true)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, scope);

            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur en récupérant "+scope+" pour "+ player.getUniqueId());
            e.printStackTrace();
        }
    }

    /* API */
    /**
     * @return La liste des recompenses
     */
    private Set<String> getRewards(){
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("rewards");
        if (section == null) return new HashSet<>();

        return section.getKeys(false);
    }

    private ConfigurationSection getRewardSection(String name){
        return plugin.getConfig().getConfigurationSection("rewards."+name);
    }

    /**
     * Donne un ItemStack a partir d'un nom de récompense
     * @param reward Nom de la recompense
     * @return ItemStack
     */
    private ItemStack getRewardAsItemStack(String reward) {
        CustomStack customStack = CustomStack.getInstance(getRewardSection(reward).getString("reward"));
        if (customStack == null) return null;
        return customStack.getItemStack();
    }

    private boolean isTooEarly(String reward) {
        long unixTime = System.currentTimeMillis() / 1000L;

        if (!getRewards().contains(reward)) { return false; }
        ConfigurationSection section = getRewardSection(reward);
        return unixTime < section.getLong("start");
    }

    private boolean isTooLate(String reward) {
        long unixTime = System.currentTimeMillis() / 1000L;

        if (!getRewards().contains(reward)) { return false; }
        ConfigurationSection section = getRewardSection(reward);
        return unixTime > section.getLong("end");
    }
    /* --- */


    @Command("rewards")
    public void rewards(CommandSender sender, @Named("Scope") String scope) {
        if (!(sender instanceof Player player)) { return; }

        if (!getRewards().contains(scope)) {
            player.sendMessage("§cCode invalide!");
            return;
        }

        if (isTooEarly(scope)) {
            player.sendMessage("§cCette récompense n'est pas encore disponible, reviens plus tard");
            return;
        }

        if (isTooLate(scope)) {
            player.sendMessage("§cTu est arrivé en retard! Peu être la prochaine fois");
            return;
        }

        if (hasClaimReward(player, scope)) {
            player.sendMessage("§cTu as déjà récuperer cette récompense!");
            return;
        }

        ItemStack item = getRewardAsItemStack(getRewardSection(scope).getString("reward"));
        if (item != null) {
            claim(player, scope);
            player.getInventory().addItem(item);
        } else {
            player.sendMessage("Une erreur est survenue");
        }
    }
}
