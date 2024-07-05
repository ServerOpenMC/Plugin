package fr.communaywen.core.teams.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemberListMenu extends Menu {

    private final Team team;

    public MemberListMenu(Player owner, Team team) {
        super(owner);
        this.team = team;
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
        content.put(49, new ItemBuilder(this, Material.ARROW, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GRAY + "Retour");
        }).setBackButton());
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
