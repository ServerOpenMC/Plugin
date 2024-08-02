package fr.communaywen.core.scoreboard;

import fr.communaywen.core.AywenCraftPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GlobalTeamManager {

    private final AywenCraftPlugin plugin;
    private final LuckPerms luckPerms;
    private final Map<UUID, Scoreboard> playerScoreboards;

    public GlobalTeamManager(AywenCraftPlugin plugin, Map<UUID, Scoreboard> playerScoreboards) {
        this.plugin = plugin;
        this.luckPerms = plugin.api;
        this.playerScoreboards = playerScoreboards;

        createTeams();
    }

    public void createTeams() {
        for (Scoreboard scoreboard : playerScoreboards.values()) {
            createTeam(scoreboard, "a", "admin");
            createTeam(scoreboard, "b", "modo");
            createTeam(scoreboard, "c", "default");
        }
    }

    private void createTeam(Scoreboard scoreboard, String teamName, String groupName) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        team.setPrefix(Objects.requireNonNull(ChatColor.translateAlternateColorCodes('&', luckPerms.getGroupManager().getGroup(groupName)
                .getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix())));
    }

    public void updatePlayerTeam(Player player) {
        for (Scoreboard scoreboard : playerScoreboards.values()) {
            // Ensure teams are created for this scoreboard
            createTeamsIfNotExists(scoreboard);

            int teamIndex = getTeam(player);
            scoreboard.getTeam("a").removeEntry(player.getName());
            scoreboard.getTeam("b").removeEntry(player.getName());
            scoreboard.getTeam("c").removeEntry(player.getName());
            switch (teamIndex) {
                case 0: // Admin
                    scoreboard.getTeam("a").addEntry(player.getName());
                    break;
                case 1: // Mod
                    scoreboard.getTeam("b").addEntry(player.getName());
                    break;
                case 2: // Player
                    scoreboard.getTeam("c").addEntry(player.getName());
                    break;
                default:
                    break;
            }
        }
    }

    private void createTeamsIfNotExists(Scoreboard scoreboard) {
        if (scoreboard.getTeam("a") == null) {
            createTeam(scoreboard, "a", "admin");
        }
        if (scoreboard.getTeam("b") == null) {
            createTeam(scoreboard, "b", "modo");
        }
        if (scoreboard.getTeam("c") == null) {
            createTeam(scoreboard, "c", "default");
        }
    }

    private int getTeam(Player player) {
        if (player.hasPermission("group.admin")) return 0;
        if (player.hasPermission("group.modo")) return 1;
        return 2;
    }
}
