package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.utils.MethodState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        return true;
    }

    public MethodState removePlayer(UUID player) {
        if (players.size() - 1 == 0) {
            players.remove(player);
            if (!AywenCraftPlugin.getInstance().getTeamManager().deleteTeam(this)) {
                players.add(player);
                return MethodState.INVALID;
            }
            return MethodState.WARNING;
        }
        if (isOwner(player)) {
            owner = getRandomPlayer();
        }
        return MethodState.VALID;
    }

    public boolean isOwner(UUID player) {
        return owner.equals(player);
    }

    private UUID getRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }
}