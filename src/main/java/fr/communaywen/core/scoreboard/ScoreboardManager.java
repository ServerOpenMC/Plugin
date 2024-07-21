package fr.communaywen.core.scoreboard;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManagers {

    public List<Player> disableSBPlayerList = new ArrayList<>();

    public void setScoreboard(Player player) {
        User userlp = AywenCraftPlugin.getInstance().api.getUserManager().getUser(player.getUniqueId());
        QueryOptions queryOptions = AywenCraftPlugin.getInstance().api.getContextManager().getQueryOptions(userlp).orElse(QueryOptions.defaultContextualOptions());
        ScoreboardManager scoreboardManagers = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManagers.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sb_aywen", "dummy");
        objective.setDisplayName("\uE253");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        TeamManager teams = AywenCraftPlugin.getInstance().getTeamManager();
        boolean teambool = teams.isInTeam(player.getUniqueId());
        Team teamName = teams.getTeamByPlayer(player.getUniqueId());
        Score vide1 = objective.getScore(" ");
        Score vide2 = objective.getScore("  ");
        Score vide3 = objective.getScore("   ");

        Score bars1 = objective.getScore("§d§m                        ");
        Score bars2 = objective.getScore("§d§m                         §r");

        Score pseudo = objective.getScore("§8• §fPseudo§7: §b" + player.getName());
        Score money = objective.getScore("§8• §fArgent§7: §6" + AywenCraftPlugin.getInstance().economyManager.getBalance(player));
        Score grade = objective.getScore("§8• §fGrade§7: §r" + (userlp.getCachedData().getMetaData(queryOptions).getPrefix() != null ? userlp.getCachedData().getMetaData(queryOptions).getPrefix().replace("&", "§") : "§7Aucun grade"));
        Score team = objective.getScore("§8• §fTeam§7: " + (teambool ? "§a" + teamName.getName() : "§7Aucune team."));

        vide1.setScore(10);
        bars1.setScore(9);
        pseudo.setScore(8);
        vide2.setScore(7);
        grade.setScore(6);
        money.setScore(5);
        vide3.setScore(4);
        team.setScore(3);
        bars2.setScore(1);

        player.setScoreboard(scoreboard);
    }
}