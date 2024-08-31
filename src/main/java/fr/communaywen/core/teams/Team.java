package fr.communaywen.core.teams;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.cache.TeamCache;
import fr.communaywen.core.teams.utils.MethodState;
import fr.communaywen.core.utils.database.DatabaseConnector;
import fr.communaywen.core.utils.serializer.BukkitSerializer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Team implements Listener {

    @Getter
    private UUID owner;
    @Getter
    private final String name;
    @Getter
    private final List<UUID> players = new ArrayList<>();
    @Getter
    private final Inventory inventory;

    private final AywenCraftPlugin plugin;
    private final TeamCache cacheManager;

    public Team(UUID owner, String name, AywenCraftPlugin plugin, TeamCache cacheManager) {
        this.plugin = plugin;
        this.owner = owner;
        this.name = name;
        this.cacheManager = cacheManager;
        this.inventory = Bukkit.createInventory(null, 27, name + " - Inventory");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.players.addAll(cacheManager.getTeamPlayers(name));
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        if (inv.equals(inventory)) {
            if (inv.getViewers().size() > 1) {
                Player other = (Player) inv.getViewers().get(0);
                e.getPlayer().sendMessage(ChatColor.RED + other.getName() + " est déjà entrain de regarder l'inventaire de team");
                e.getPlayer().closeInventory();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() == inventory) {
            try {
                PreparedStatement statement = plugin.getManagers().getDatabaseManager().getConnection().prepareStatement("UPDATE teams SET inventory = ? WHERE teamName = ?");
                statement.setBytes(1, new BukkitSerializer().serializeItemStacks(inventory.getContents()));
                statement.setString(2, name);
                statement.executeUpdate();
            } catch (Exception exc) {
                plugin.getLogger().severe("Impossible de sauvegarder l'inventaire de la team '" + this.name + "'");
            }
        }
    }

    public void delete() {
        cacheManager.getTeams().remove(this.name);
    }

    public boolean isIn(UUID player) {
        return players.contains(player);
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public void setInventory(ItemStack[] newinv) {
        if (newinv == null) {
            return;
        }
        inventory.setContents(newinv);
    }

    public boolean giveClaimStick(Player player) {
        CustomStack customStack = CustomStack.getInstance("claim_stick");
        if(customStack != null) {
            ItemStack itemStack = customStack.getItemStack();
            player.getInventory().addItem(itemStack);
            return true;
        } else {
            return false;
        }
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
        cacheManager.addPlayer(this.name, player);
        return true;
    }

    public boolean addPlayerWithoutSave(UUID player) {
        if (players.size() >= 20) {
            return false;
        }
        players.add(player);
        return true;
    }

    public void sendMessage(Player sender, String message){
        players.stream()
                .filter(player -> Bukkit.getPlayer(player) != null)
                .forEach(player -> Bukkit.getPlayer(player).sendMessage("§aᴛᴇᴀᴍ ᴄʜᴀᴛ §7» §b" + sender.getName() + " §7- §f" + message));
    }

    public MethodState removePlayer(UUID player) {
        players.remove(player);
        cacheManager.removePlayer(this.name, player);
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
        return MethodState.VALID;
    }

    public boolean isOwner(UUID player) {
        return owner.equals(player);
    }

    private UUID getRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
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
}
