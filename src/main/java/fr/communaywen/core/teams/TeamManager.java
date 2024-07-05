package fr.communaywen.core.teams;

import fr.communaywen.core.utils.Queue;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    // Laissez ce truc
    // Zeub - By Xernas 05/07/2024 à 00:05 (UTC+2)
    // Le createur de ce truc
    private final List<Team> teams = new ArrayList<>();
    private final Queue<OfflinePlayer, Team> pendingInvites = new Queue<>(20);

    public List<Team> getTeams() {
        return teams;
    }

    public Team createTeam(OfflinePlayer owner, String name) {
        Team team = new Team(owner, name);
        teams.add(team);
        return team;
    }

    public void deleteTeam(Team team) {
        teams.remove(team);
    }

    public Team isInTeam(OfflinePlayer player) {
        for (Team team : teams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public boolean invite(OfflinePlayer player, Team team) {
        if (team.getPlayers().size() >= 20) {
            return false;
        }
        pendingInvites.add(player, team);
        return true;
    }

    public Team acceptInvite(OfflinePlayer player) {
        Team team = pendingInvites.get(player);
        if (team != null) {
            team.addPlayer(player);
            pendingInvites.remove(player);
        }
        return team;
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

}
