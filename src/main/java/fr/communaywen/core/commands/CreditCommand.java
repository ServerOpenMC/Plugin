package fr.communaywen.core.commands;

import fr.communaywen.core.utils.GitHubAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CreditCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
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
        } else {
            sender.sendMessage("Cette commande peut seulement être éxécutée par un joueur.");
        }
        return true;
    }
}
