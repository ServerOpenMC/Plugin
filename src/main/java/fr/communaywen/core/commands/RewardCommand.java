package fr.communaywen.core.commands;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        }
    }

    @Command("rewards")
    public void rewards(CommandSender sender, @Named("Scope") String scope) {
        if (!(sender instanceof Player player)) { return; }
        // TODO: Rendre plus "robuste" pour les futurs codes

        long unixTime = System.currentTimeMillis() / 1000L;
        if (scope.equals("peluche")) {
            if (unixTime >= 1722636000L) { //Sat Aug 03 2024 00:00:00 GMT+0200, Une semaine après le 27
                player.sendMessage("L'évenement est terminé :'( peut être une prochaine fois");
                return;
            }

            if (hasClaimReward(player, "peluche")) {
                player.sendMessage("§cTu as déjà récupérer cette récompense");
                return;
            }

            CustomStack peluche = CustomStack.getInstance("aywen:peluche");
            if (peluche != null) {
                claim(player, "peluche");
                player.getInventory().addItem(peluche.getItemStack());
            } else {
                player.sendMessage("Une erreur est survenue");
            }
        } else {
            player.sendMessage("§cCode invalide!");
        }
    }
}
