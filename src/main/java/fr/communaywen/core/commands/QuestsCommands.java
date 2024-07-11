package fr.communaywen.core.commands;

import fr.communaywen.core.quests.QuestsMenu;
import fr.communaywen.core.utils.GitHubAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.io.IOException;

public class QuestsCommands {
    @Command({"quest", "quests", "quete", "quetes"})
    @Description("Système de quêtes.")
    public void onCommand(Player player) {
        QuestsMenu menu = new QuestsMenu(player);
        menu.open();
    }
}
