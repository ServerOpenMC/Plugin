package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Team {

    private UUID owner;
    private final String name;
    private final List<UUID> players = new ArrayList<>();
    private final Inventory inventory;

    public Team(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.inventory = Bukkit.createInventory(null , 27, name + " - Inventory");
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public List<UUID> getPlayers(int first, int last) {
        List<UUID> result = new ArrayList<>();
        for (int i = first; i < last; i++) {
            UUID player = getPlayer(i);
            if (player != null) {
                result.add(player);
            }
        }
        return result;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public UUID getPlayerByUsername(String username) {
        Player bukkitPlayer = Bukkit.getPlayer(username);
        if (bukkitPlayer == null) {
            return null;
        }
        for (UUID player : players) {
            if (Objects.equals(player, bukkitPlayer.getUniqueId())) {
                return player;
            }
        }
        return null;
    }

    public UUID getPlayer(int index) {
        if (index < 0 || index >= players.size()) {
            return null;
        }
        return players.get(index);
    }

    public boolean addPlayer(UUID player) {
        if (players.size() >= 20) {
            return false;
        }
        players.add(player);
        return true;
    }

    public boolean removePlayer(UUID player) {
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

    public boolean isOwner(UUID player) {
        return owner.equals(player);
    }

    private UUID getRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }
}
