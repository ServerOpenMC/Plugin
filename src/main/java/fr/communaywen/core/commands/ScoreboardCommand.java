package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.scoreboard.ScoreboardManagers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class ScoreboardCommand {
    ScoreboardManagers scoreboardManagers = AywenCraftPlugin.getInstance().scoreboardManagers;

    @Command({"scoreboard", "sb"})
    @Description("Désactive / active ton scoreboard")
    public void onDelete(Player player) {
        if(scoreboardManagers.disableSBPlayerList.contains(player)) {
            scoreboardManagers.disableSBPlayerList.remove(player);
            player.sendMessage("§aScoreboard activé !");
            scoreboardManagers.setScoreboard(player);
        } else {
            scoreboardManagers.disableSBPlayerList.add(player);
            Scoreboard emptySB = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(emptySB);
            player.sendMessage("§cScoreboard désactivé !");
        }
    }
}