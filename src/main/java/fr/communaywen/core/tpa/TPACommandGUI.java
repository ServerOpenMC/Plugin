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
        buttons.put(48, createButton(Material.RED_CONCRETE, "§cPrevious", "previous_page"));
        buttons.put(49, createButton(Material.BARRIER, "§7Close", "close"));
        buttons.put(50, createButton(Material.GREEN_CONCRETE, "§aNext", "next_page"));
        return buttons;
    }

    private ItemStack createButton(Material material, String name, String itemId) {
        return new ItemBuilder(this, material, itemMeta -> {
            itemMeta.setDisplayName(name);
            itemMeta.getPersistentDataContainer().set(MenuLib.getItemIdKey(), PersistentDataType.STRING, itemId);
        }).build();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        String itemId = MenuLib.getItemId(event.getCurrentItem());
        if (itemId == null) return;

        switch (itemId) {
            case "previous_page":
                if (!isFirstPage()) {
                    setPage(getPage() - 1);
                    open();
                }
                break;
            case "next_page":
                if (!isLastPage()) {
                    setPage(getPage() + 1);
                    open();
                }
                break;
            case "close":
                getOwner().closeInventory();
                break;
            default:
                break;
        }
    }
}
