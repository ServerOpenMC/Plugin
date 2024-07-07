package fr.communaywen.core.commands;

import fr.communaywen.core.utils.GitHubAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.io.IOException;

public class CreditCommand {
    @Command("credit")
    @Description("Affiche les crédits du plugin")
    public void onCommand(Player player) {
        GitHubAPI gitHubAPI = new GitHubAPI();
        try {
            String jsonResponse = gitHubAPI.getContributors();
            if (jsonResponse.startsWith("Error:")) {
                player.sendMessage("Impossible de récuperer les contributeurs: " + jsonResponse);
            } else {
                JSONArray contributors = new JSONArray(jsonResponse);
                player.sendMessage(ChatColor.BOLD+"Merci à"+ChatColor.RESET);
                for (int i = 0; i < contributors.length(); i++) {
                    JSONObject contributor = contributors.getJSONObject(i);
                    String login = contributor.getString("login");
                    player.sendMessage("- " + ChatColor.GOLD + login + ChatColor.RESET);
                }
            }
        } catch (IOException e) {
            player.sendMessage("Une erreur est apparue lors de la récuperation des contributeurs.");
            e.printStackTrace();
        }
    }
}
