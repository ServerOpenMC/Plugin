package fr.communaywen.core.scoreboard;

import fr.communaywen.core.AywenCraftPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

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
        List<Group> groups = new ArrayList<>(luckPerms.getGroupManager().getLoadedGroups());

        groups.sort((g1, g2) -> Integer.compare(g2.getWeight().orElse(0), g1.getWeight().orElse(0)));

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            String teamName = String.valueOf((char) ('a' + i));
            for (Scoreboard scoreboard : playerScoreboards.values()) {
                createTeam(scoreboard, teamName, group);
            }
        }
    }

    private void createTeam(Scoreboard scoreboard, String teamName, Group group) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        team.setPrefix(Objects.requireNonNull(ChatColor.translateAlternateColorCodes('&',
                group.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix())));
    }

    public void updatePlayerTeam(Player player) {
        for (Scoreboard scoreboard : playerScoreboards.values()) {
            createTeamsIfNotExists(scoreboard);

            Group playerGroup = luckPerms.getUserManager().getUser(player.getUniqueId())
                    .getNodes(NodeType.INHERITANCE).stream()
                    .map(NodeType.INHERITANCE::cast)
                    .map(inheritanceNode -> luckPerms.getGroupManager().getGroup(inheritanceNode.getGroupName()))
                    .max(Comparator.comparingInt(group -> group.getWeight().orElse(0)))
                    .orElse(null);

            if (playerGroup != null) {
                String teamName = getTeamNameByGroup(playerGroup);
                if (teamName != null) {
                    for (Team team : scoreboard.getTeams()) {
                        team.removeEntry(player.getName());
                    }
                    scoreboard.getTeam(teamName).addEntry(player.getName());
                }
            }
        }
    }

    private void createTeamsIfNotExists(Scoreboard scoreboard) {
        for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
            String teamName = getTeamNameByGroup(group);
            if (teamName != null && scoreboard.getTeam(teamName) == null) {
                createTeam(scoreboard, teamName, group);
            }
        }
    }

    private String getTeamNameByGroup(Group group) {
        List<Group> groups = new ArrayList<>(luckPerms.getGroupManager().getLoadedGroups());

        groups.sort((g1, g2) -> Integer.compare(g2.getWeight().orElse(0), g1.getWeight().orElse(0)));

        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).equals(group)) {
                return String.valueOf((char) ('a' + i));
            }
        }
        return null;
    }
}
