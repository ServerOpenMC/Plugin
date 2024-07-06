package fr.communaywen.core.teams.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.utils.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamMenu extends Menu {

    private final Team team;
    private final boolean fromList;

    public TeamMenu(Player owner, Team team, boolean fromList) {
        super(owner);
        this.team = team;
        this.fromList = fromList;
    }

    @Override
    public @NotNull String getName() {
        return team.getName();
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = fill(Material.GRAY_STAINED_GLASS_PANE);
        if (fromList) {
            content.put(49, new ItemBuilder(this, Material.ARROW, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GRAY + "Retour");
            }).setBackButton());
        } else {
            content.put(49, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.RED + "Fermer");
            }).setCloseButton());
        }
        if (team.isIn(getOwner().getUniqueId())) {
            content.put(45, new ItemBuilder(this, Material.LIME_WOOL, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GREEN + "Vous êtes dans cette équipe");
            }));
            content.put(46, new ItemBuilder(this, Material.RED_DYE, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.RED + "Quitter l'équipe");
            }).setNextMenu(new ConfirmationMenu(getOwner(), event -> {
                TeamUtils.quit(team, getOwner());
                getOwner().closeInventory();
            })));
            content.put(53, new ItemBuilder(this, Material.CHEST, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + "Inventaire de la team");
            }).setOnClick(event -> {
                team.openInventory(getOwner());
            }));
        } else {
            content.put(45, new ItemBuilder(this, Material.RED_WOOL, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.RED + "Vous n'êtes pas dans cette équipe");
            }));
        }
        List<UUID> firstSixPlayers = team.getPlayers(0, 7);
        List<UUID> secondSixPlayers = team.getPlayers(7, 14);
        List<UUID> thirdFivePlayers = team.getPlayers(14, 20);
        UUID lastPlayer = team.getPlayer(20);
        for (int i = 0; i < firstSixPlayers.size(); i++) {
            int finalI = i;
            content.put(i + 10, new ItemBuilder(this, ItemUtils.getPlayerSkull(firstSixPlayers.get(i)), itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + Bukkit.getOfflinePlayer(firstSixPlayers.get(finalI)).getName())));
        }
        for (int i = 7; i < secondSixPlayers.size(); i++) {
            int finalI = i;
            content.put(i + 12, new ItemBuilder(this, ItemUtils.getPlayerSkull(secondSixPlayers.get(i)), itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + Bukkit.getOfflinePlayer(secondSixPlayers.get(finalI)).getName())));
        }
        for (int i = 14; i < thirdFivePlayers.size(); i++) {
            int finalI = i;
            content.put(i + 15, new ItemBuilder(this, ItemUtils.getPlayerSkull(thirdFivePlayers.get(i)), itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + Bukkit.getOfflinePlayer(thirdFivePlayers.get(finalI)).getName())));
        }
        if (lastPlayer != null) {
            content.put(40, new ItemBuilder(this, ItemUtils.getPlayerSkull(lastPlayer), itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + Bukkit.getOfflinePlayer(lastPlayer).getName())));
        }
        return content;
    }
}