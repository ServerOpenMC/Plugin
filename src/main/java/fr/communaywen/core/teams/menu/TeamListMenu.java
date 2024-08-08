package fr.communaywen.core.teams.menu;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.teams.Team;
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
import java.util.UUID;

public class TeamListMenu extends PaginatedMenu {

    private final TeamManager manager;
    // Meekiavelique : Items mis en cache pour éviter de les recréer à chaque appel de getItems()
    private final Map<Integer, ItemStack> cachedItems = new HashMap<>();

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
        // Meekiavelique : Récuperer la liste des teams une seule fois et itére dessus, plutôt que d'appeler manager.getTeams().get(i) plusieurs fois
        List<Team> teams = manager.getTeams();
        for (int i = 0; i < teams.size(); i++) {
            items.add(getTeamItem(i, teams.get(i)));
        }
        return items;
    }

    // Meekiavelique : Crée des items uniquement si nécessaire en utilisant computeIfAbsent
    private ItemStack getTeamItem(int index, Team team) {
        return cachedItems.computeIfAbsent(index, k -> {
            UUID ownerUUID = team.getOwner();
            String ownerName = Bukkit.getOfflinePlayer(ownerUUID).getName();
            return new ItemBuilder(this, ItemUtils.getPlayerSkull(ownerUUID), itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + team.getName());
                itemMeta.setLore(List.of(
                        ChatColor.DARK_RED + "Propriétaire: " + ownerName,
                        ChatColor.GRAY + "■ Membres: " + team.getPlayers().size(),
                        ChatColor.GRAY + "■ Cliquez pour voir les détails"
                ));
            });
        });
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
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;  // Meekiavelique : Ignore les clicks sur les slots vide
        }

        if (!getStaticSlots().contains(slot)) {
            int index = getPage() * (getInventorySize().getSize() - getStaticSlots().size()) + slot;
            List<Team> teams = manager.getTeams();
            if (index < teams.size()) {
                Team selectedTeam = teams.get(index);
                new TeamMenu(getOwner(), selectedTeam, true).open();
            }
        }
    }
}