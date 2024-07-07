package fr.communaywen.core.teams.menu;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamListMenu extends PaginatedMenu {

    private final TeamManager manager;

    public TeamListMenu(Player owner, TeamManager manager) {
        super(owner);
        this.manager = manager;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.GRAY_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return StaticSlots.STANDARD;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < manager.getTeams().size(); i++) {
            int finalI = i;
            items.add(new ItemBuilder(this, ItemUtils.getPlayerSkull(manager.getTeams().get(i).getOwner()), itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + manager.getTeams().get(finalI).getName());
                itemMeta.setLore(List.of(
                        ChatColor.DARK_RED + "Propriétaire: " + Bukkit.getOfflinePlayer(manager.getTeams().get(finalI).getOwner()).getName(),
                        ChatColor.GRAY + "■ Membres: " + manager.getTeams().get(finalI).getPlayers().size(),
                        ChatColor.GRAY + "■ Cliquez pour voir les détails"
                ));
            }).setNextMenu(new TeamMenu(getOwner(), manager.getTeams().get(i), true)));
        }
        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> map = new HashMap<>();
        map.put(49, new ItemBuilder(this, Material.BARRIER, itemMeta -> itemMeta.setDisplayName(ChatColor.GRAY + "Fermer"))
                .setCloseButton());
        map.put(48, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> itemMeta.setDisplayName(ChatColor.RED + "Page précédente"))
                .setPreviousPageButton());
        map.put(50, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + "Page suivante"))
                .setNextPageButton());
        return map;
    }

    @Override
    public @NotNull String getName() {
        return "Liste des teams";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}