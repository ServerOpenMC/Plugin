package fr.communaywen.core.tpa;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TPAMenu extends Menu {

    private final int currentPage;
    private static final int PLAYERS_PER_PAGE = 45;

    public TPAMenu(Player player, int currentPage) {
        super(player);
        this.currentPage = currentPage;
    }

    public TPAMenu(Player player) {
        this(player, 0);
    }

    @Override
    public @NotNull String getName() {
        return "§9§lSélectionnez un joueur (Page " + (currentPage + 1) + ")";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGE;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        Player player = getOwner().getPlayer();

        if (clickedItem.getType() == Material.ARROW) {
            String itemName = clickedItem.getItemMeta().getDisplayName();
            if (itemName.equals("§aPage suivante")) {
                new TPAMenu(player, currentPage + 1).open();
            } else if (itemName.equals("§aPage précédente")) {
                new TPAMenu(player, currentPage - 1).open();
            }
        } else if (clickedItem.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            if (meta != null && meta.getOwningPlayer() != null) {
                Player target = meta.getOwningPlayer().getPlayer();
                if (target != null) {
                    TPACommand.sendTPARequest(player, target);
                    player.closeInventory();
                }
            }
        }
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();
        List<Player> onlinePlayers = new ArrayList<>(getOwner().getPlayer().getServer().getOnlinePlayers());

        int startIndex = currentPage * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, onlinePlayers.size());

        for (int i = startIndex; i < endIndex; i++) {
            Player onlinePlayer = onlinePlayers.get(i);
            if (!onlinePlayer.equals(getOwner().getPlayer())) {
                content.put(i - startIndex, createPlayerHead(onlinePlayer));
            }
        }

        if (currentPage > 0) {
            content.put(45, createNavigationItem("§aPage précédente", Material.ARROW));
        }
        if (endIndex < onlinePlayers.size()) {
            content.put(53, createNavigationItem("§aPage suivante", Material.ARROW));
        }

        return content;
    }

    private ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.setDisplayName("§a" + player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }

    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
