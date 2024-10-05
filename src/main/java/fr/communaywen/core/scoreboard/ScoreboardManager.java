package fr.communaywen.core.scoreboard;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.FirerocketSpawnListener;
import fr.communaywen.core.dreamdim.listeners.CloudSoup;
import fr.communaywen.core.spawn.head.HeadManager;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ScoreboardManager {

    private final AywenCraftPlugin plugin;
    private final LuckPerms luckPerms;
    private final TeamManager teamManager;
    private final GlobalTeamManager globalTeamManager;
    private final Map<UUID, Scoreboard> playerScoreboards;
    public List<UUID> disabledPlayers = new ArrayList<>();

    public ScoreboardManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        this.luckPerms = plugin.api;
        this.teamManager = plugin.getManagers().getTeamManager();
        this.playerScoreboards = new HashMap<>();

        this.globalTeamManager = new GlobalTeamManager(plugin, playerScoreboards);

        Bukkit.getScheduler().runTaskTimer(plugin, this::updateAllScoreboards, 0L, 20L * 5);
    }

    public void setScoreboard(Player player) {
        if (disabledPlayers.contains(player.getUniqueId())) return;

        Scoreboard scoreboard = playerScoreboards.computeIfAbsent(player.getUniqueId(), k -> createNewScoreboard(player));
        player.setScoreboard(scoreboard);
    }

    private Scoreboard createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sb_aywen", "dummy", PlaceholderAPI.setPlaceholders(player, "%img_openmc%"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        updateScoreboard(player, scoreboard, objective);

        globalTeamManager.createTeams();

        return scoreboard;
    }

    private void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (disabledPlayers.contains(player.getUniqueId())) continue;

            updateScoreboard(player);
        }
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) return;

        Objective objective = scoreboard.getObjective("sb_aywen");
        if (objective == null) return;

        updateScoreboard(player, scoreboard, objective);
    }

    private void updateScoreboard(Player player, Scoreboard scoreboard, Objective objective) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        User userlp = luckPerms.getUserManager().getUser(player.getUniqueId());
        QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(userlp).orElse(QueryOptions.defaultContextualOptions());
        boolean teambool = teamManager.isInTeam(player.getUniqueId());
        Team teamName = teamManager.getTeamByPlayer(player.getUniqueId());
        String flytime = CloudSoup.getInstance().getFlyTime(player);
        String ipStr = "ᴘʟᴀʏ.ᴏᴘᴇɴᴍᴄ.ꜰʀ";

        objective.getScore(" ").setScore(13);
        objective.getScore("§d§m                        ").setScore(12);
        objective.getScore("§8• §fPseudo§7: §b" + player.getName()).setScore(11);
        objective.getScore("  ").setScore(10);

        if (flytime != null) {
            objective.getScore("§8• §fVol§7: §a" + flytime).setScore(9);
        }

        objective.getScore("§8• §fGrade§7: §r" + (userlp.getCachedData().getMetaData(queryOptions).getPrefix() != null ? userlp.getCachedData().getMetaData(queryOptions).getPrefix().replace("&", "§") : "§7Aucun grade")).setScore(8);
        objective.getScore("§8• §fArgent§7: §6" + plugin.getManagers().getEconomyManager().getBalance(player)).setScore(7);
        objective.getScore("   ").setScore(6);
        objective.getScore("§8• §fTeam§7: " + (teambool ? "§a" + teamName.getName() : "§7Aucune team.")).setScore(5);
        if (FirerocketSpawnListener.isPlayerInRegion("spawn", Bukkit.getWorld("world"))) {
            int heads = HeadManager.getHeadFoundIntCache(player);
            int maxHeads = HeadManager.getMaxHeads();

            objective.getScore("   ").setScore(5);
            objective.getScore("§8• §fHeads§7: §d" + heads + "§8/§d"+ maxHeads).setScore(4);
        }
        /**
        if(phase != 1) {
            objective.getScore(" ").setScore(4);
            objective.getScore("§8• §6§lCONTEST!").setScore(3);
            objective.getScore(ChatColor.valueOf(ContestManager.getColor1Cache()) + ContestManager.getCamp1Cache() + " §8VS " + ChatColor.valueOf(ContestManager.getColor2Cache())  + ContestManager.getCamp2Cache()).setScore(2);
        }
        **/
        objective.getScore("§d§m                         §r").setScore(1);
        objective.getScore("§d    " + ipStr).setScore(0);

        globalTeamManager.updatePlayerTeam(player);
    }
}
