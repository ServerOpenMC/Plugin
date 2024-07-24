package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.utils.MethodState;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Team extends DatabaseConnector {

    private UUID owner;
    private final String name;
    private final List<UUID> players = new ArrayList<>();
    private final Inventory inventory;

    public Team(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.inventory = Bukkit.createInventory(null, 27, name + " - Inventory");
    }

    public void delete() throws SQLException {
        // Remove players from teams_players
        PreparedStatement statement = connection.prepareStatement("DELETE FROM teams_player WHERE teamName = ?");
        statement.setString(1, this.name);
        statement.executeUpdate();

        // Remove team from teams
        statement = connection.prepareStatement("DELETE FROM teams WHERE teamName = ?");
        statement.setString(1, this.name);
        statement.executeUpdate();
    }

    public void save(){
        try {
            delete();

            for (UUID player : this.getPlayers()){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO teams_player VALUES (?, ?)");
                statement.setString(1, this.name);
                statement.setString(2, player.toString());
                statement.executeUpdate();
            }

            PreparedStatement statement = connection.prepareStatement("INSERT INTO teams VALUES (?, ?, 0)");
            statement.setString(1, this.name);
            statement.setString(2, this.owner.toString());
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("\u001B[31mErreur en sauvegardant la team '"+this.name+"'\u001B[0m");
        }
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

    public boolean isIn(UUID player) {
        return players.contains(player);
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public void giveClaimStick(Player player) {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§cBATON DE CLAIM");
        itemMeta.addEnchant(Enchantment.SHARPNESS, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(Arrays.asList("§6Faites deux cliques §egauches §6", "§6sur l'endroit où vous voulez claim.", "§6Toutes les locations de la couche §e-62 §6à §e320 §6seront protégés."));

        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);
    }

    public Inventory getInventory() {
        return inventory;
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
        save();
        return true;
    }

    public boolean addPlayerWithoutSave(UUID player) {
        if (players.size() >= 20) {
            return false;
        }
        players.add(player);
        return true;
    }

    public MethodState removePlayer(UUID player) {
        players.remove(player);
        if (players.isEmpty()) {
            if (!AywenCraftPlugin.getInstance().getManagers().getTeamManager().deleteTeam(this)) {
                players.add(player);
                return MethodState.INVALID;
            }
            return MethodState.WARNING;
        }
        if (isOwner(player)) {
            owner = getRandomPlayer();
        }
        save();
        return MethodState.VALID;
    }

    public boolean isOwner(UUID player) {
        return owner.equals(player);
    }

    private UUID getRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }
}