package fr.communaywen.core.commands.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class ScoreboardCommand {
    ScoreboardManager scoreboardManager = AywenCraftPlugin.getInstance().getManagers().getScoreboardManager();

    @Command({"scoreboard", "sb"})
    @Description("Désactive / active ton scoreboard")
    public void onDelete(Player player) {
        if (scoreboardManager.disabledPlayers.contains(player.getUniqueId())) {
            scoreboardManager.disabledPlayers.remove(player.getUniqueId());
            scoreboardManager.setScoreboard(player);
            player.sendMessage("§aScoreboard activé !");
        } else {
            scoreboardManager.disabledPlayers.add(player.getUniqueId());
            Scoreboard emptySB = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(emptySB);
            player.sendMessage("§cScoreboard désactivé !");
        }
    }
}