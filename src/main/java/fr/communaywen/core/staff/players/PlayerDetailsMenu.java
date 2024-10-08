package fr.communaywen.core.staff.players;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.FreezeUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDetailsMenu extends Menu {
    private final Player target;
    private final AywenCraftPlugin instance = AywenCraftPlugin.getInstance();
    private final TeamManager teamManager = new TeamManager(instance);

    public PlayerDetailsMenu(Player owner, Player target) {
        super(owner);
        this.target = target;
    }

    @Override
    public @NotNull String getName() {
        return ChatColor.BLUE + "Détails";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> map = new HashMap<>();
        map.put(0, new ItemBuilder(this, Material.PAPER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Retour");
            itemMeta.setCustomModelData(10005);
        }).setBackButton());

        map.put(1, new ItemBuilder(this, Material.COMPASS, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Position/Cliquer pour se téléporter");
            itemMeta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getLocation().getBlockX()) + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()));
        }).setOnClick(inventoryClickEvent -> {
            if (!checkAuthorized()) return;
            getOwner().teleport(target.getLocation());
            getOwner().sendMessage(ChatColor.DARK_GREEN + "Vous avez été téléporté au joueur " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_GREEN + " !");
            getOwner().closeInventory();
        }));

        map.put(2, new ItemBuilder(this, Material.APPLE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Vie");
            itemMeta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getHealth())));
        }));

        map.put(3, new ItemBuilder(this, Material.COOKED_BEEF, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Faim");
            itemMeta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getFoodLevel())));
        }));

        map.put(4, new ItemBuilder(this, ItemUtils.getPlayerSkull(target.getUniqueId()), itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + target.getDisplayName());
            itemMeta.setLore(List.of(ChatColor.BLUE + "Team : " + teamManager.getTeamByPlayer(target.getUniqueId()).getName()));
        }));

        map.put(5, new ItemBuilder(this, Material.EXPERIENCE_BOTTLE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Expérience");
            itemMeta.setLore(List.of(ChatColor.BLUE + "Niveau " + target.getLevel(), "Xp : " + target.getExp()));
        }));

        map.put(6, new ItemBuilder(this, Material.ENDER_EYE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Voir l'inventaire");
        }).setOnClick(inventoryClickEvent -> {
            if (!checkAuthorized() || !checkOnline()) return;
            getOwner().openInventory(target.getInventory());
        }));

        map.put(7, new ItemBuilder(this, Material.PACKED_ICE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Geler le joueur");
            itemMeta.setLore(List.of(ChatColor.BLUE + "État : " + (AywenCraftPlugin.frozenPlayers.contains(target) ? "§4Freeze" : "§2Unfreeze")));
        }).setOnClick(inventoryClickEvent -> {
            if (!checkAuthorized() || !checkOnline()) return;
            FreezeUtils.switch_freeze(getOwner(), target);
            getOwner().closeInventory();
        }));

        map.put(8, new ItemBuilder(this, Material.WOODEN_AXE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Bannir le joueur");
        }).setOnClick(inventoryClickEvent -> {
            if (!checkAuthorized()) return;
            String name = target.getName();
            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(name, "Aucune raison spécifiée", null, getOwner().getName());
            getOwner().sendMessage(ChatColor.BLUE + name + ChatColor.DARK_RED + " a bien été banni !");
            target.kickPlayer("Vous avez été banni pour aucune raison spécifiée");
            getOwner().closeInventory();
        }));
        return map;
    }

    public boolean checkOnline() {
        if (target.isOnline()) return true;

        getOwner().sendMessage(ChatColor.RED + target.getName() + " n'est plus connecté");
        getOwner().closeInventory();
        return false;
    }

    public boolean checkAuthorized() {
        if (getOwner().hasPermission("openmc.staff.players")) return true;

        getOwner().sendMessage(ChatColor.RED + "Vous n'avez pas la permission de faire cela");
        getOwner().closeInventory();
        return false;
    }
}