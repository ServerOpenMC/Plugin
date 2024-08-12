package fr.communaywen.core.tpa;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TPACommandGUI extends PaginatedMenu {

    private final AywenCraftPlugin plugin;

    public TPACommandGUI(Player owner, AywenCraftPlugin plugin) {
        super(owner);
        this.plugin = plugin;
    }

    @Override
    public Material getBorderMaterial() {
        return Material.GRAY_STAINED_GLASS_PANE;
    }

    @Override
    public List<Integer> getStaticSlots() {
        return StaticSlots.BOTTOM;
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(target -> {
            if (!target.equals(getOwner())) {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName("§a" + target.getName());
                    item.setItemMeta(meta);
                }
                items.add(item);
            }
        });
        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> buttons = new HashMap<>();
        buttons.put(48, createButton(Material.RED_CONCRETE, "§cPrevious"));
        buttons.put(49, createButton(Material.BARRIER, "§7Close"));
        buttons.put(50, createButton(Material.GREEN_CONCRETE, "§aNext"));
        return buttons;
    }

    private ItemStack createButton(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public String getName() {
        return "Sélectionnez un joueur pour TPA";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                ItemMeta meta = clickedItem.getItemMeta();
                String playerName = meta != null ? meta.getDisplayName().replace("§a", "") : "Unknown";
                Player target = Bukkit.getPlayer(playerName);
                if (target != null) {
                    TPACommand.sendTPARequest(getOwner(), target, plugin);
                    getOwner().closeInventory();
                }
            }
        }
    }
}
