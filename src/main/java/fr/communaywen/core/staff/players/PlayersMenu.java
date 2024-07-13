package fr.communaywen.core.staff.players;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.teams.menu.TeamMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersMenu extends PaginatedMenu {
    public PlayersMenu(Player owner) {
        super(owner);
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return StaticSlots.BOTTOM;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        ArrayList<Player> players = new ArrayList<>(this.getOwner().getServer().getOnlinePlayers());

        List<ItemStack> items = new ArrayList<>();
        for (Player player : players) {
            items.add(new ItemBuilder(this, ItemUtils.getPlayerSkull(player.getUniqueId()), itemMeta -> {
                System.out.println("META");
                System.out.println(itemMeta);
                itemMeta.setDisplayName(ChatColor.WHITE + player.getName());
                itemMeta.setLore(List.of(
                        ChatColor.GOLD + "Vie : " + ChatColor.RED + player.getHealth(),
                        ChatColor.GOLD + "XP : " + ChatColor.AQUA + player.getExp()
                ));
            }).setNextMenu(new PlayerDetailsMenu(this.getOwner(), player)));
        }
        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> map = new HashMap<>();
        map.put(49, new ItemBuilder(this, Material.PAPER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GRAY + "Fermer");
            itemMeta.setCustomModelData(10003);
        }).setCloseButton());
        map.put(48, new ItemBuilder(this, Material.PAPER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Page précédente");
            itemMeta.setCustomModelData(10005);
        }).setPreviousPageButton());
        map.put(50, new ItemBuilder(this, Material.PAPER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Page suivante");
            itemMeta.setCustomModelData(10006);
        }).setNextPageButton());
        return map;
    }

    @Override
    public @NotNull String getName() {
        return ChatColor.BLUE + "Liste des joueurs";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) { }
}
