package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {

    private final String name;
    private OfflinePlayer owner;
    private final List<OfflinePlayer> players = new ArrayList<>();

    public Team(OfflinePlayer owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public List<OfflinePlayer> getPlayers() {
        return players;
    }

    public List<OfflinePlayer> getPlayers(int first, int last) {
        List<OfflinePlayer> result = new ArrayList<>();
        for (int i = first; i < last; i++) {
            OfflinePlayer player = getPlayer(i);
            if (player != null) {
                result.add(player);
            }
        }
        return result;
    }

    public OfflinePlayer getPlayer(String name) {
        for (OfflinePlayer player : players) {
            if (Objects.equals(player.getName(), name)) {
                return player;
            }
        }
        return null;
    }

    public OfflinePlayer getPlayer(int index) {
        if (index < 0 || index >= players.size()) {
            return null;
        }
        return players.get(index);
    }

    public boolean addPlayer(OfflinePlayer player) {
        if (players.size() >= 20) {
            return false;
        }
        players.add(player);
        return true;
    }

    public boolean removePlayer(OfflinePlayer player) {
        players.remove(player);
        if (players.isEmpty()) {
            AywenCraftPlugin.getInstance().getTeamManager().deleteTeam(this);
            return false;
        }
        if (isOwner(player)) {
            owner = getRandomPlayer();
        }
        return true;
    }

    public boolean isOwner(OfflinePlayer player) {
        return owner.equals(player);
    }

    private OfflinePlayer getRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }
}
