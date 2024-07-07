package fr.communaywen.core.teams;

import fr.communaywen.core.utils.Queue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    // Laissez ce truc
    // Zeub - By Xernas 05/07/2024 à 00:05 (UTC+2)
    // Le createur de ce truc
    private final List<Team> teams = new ArrayList<>();
    private final Queue<UUID, Team> pendingInvites = new Queue<>(20);

    public List<Team> getTeams() {
        return teams;
    }

    public Team createTeam(UUID owner, String name) {
        Team team = new Team(owner, name);
        teams.add(team);
        return team;
    }

    public boolean deleteTeam(Team team) {
        int items = 0;
        for (ItemStack item : team.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                items++;
            }
        }
        if (items > 0) {
            return false;
        }
        teams.remove(team);
        return true;
    }

    public boolean isInTeam(UUID player) {
        for (Team team : teams) {
            if (team.getPlayers().contains(player)) {
                return true;
            }
        }
        return false;
    }

    public Team getTeamByPlayer(UUID player) {
        for (Team team : teams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public boolean invite(UUID player, Team team) {
        if (team.getPlayers().size() >= 20) {
            return false;
        }
        pendingInvites.add(player, team);
        return true;
    }

    public Team acceptInvite(UUID player) {
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