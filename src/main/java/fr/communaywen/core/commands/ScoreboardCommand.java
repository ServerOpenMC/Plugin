package fr.communaywen.core.commands;

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
        if (scoreboardManager.disableSBPlayerList.contains(player)) {
            scoreboardManager.disableSBPlayerList.remove(player);
            player.sendMessage("§aScoreboard activé !");
            scoreboardManager.setScoreboard(player);
        } else {
            scoreboardManager.disableSBPlayerList.add(player);
            Scoreboard emptySB = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(emptySB);
            player.sendMessage("§cScoreboard désactivé !");
        }
    }
}