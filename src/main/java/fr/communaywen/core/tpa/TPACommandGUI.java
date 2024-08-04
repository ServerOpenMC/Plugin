package fr.communaywen.core.tpa;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        return Material.BLACK_STAINED_GLASS_PANE;
    }

    @Override
    public List<Integer> getStaticSlots() {
        return dev.xernas.menulib.utils.StaticSlots.BOTTOM;
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(target -> {
            if (!target.equals(getOwner())) {
                ItemStack item = new ItemBuilder(this, Material.PLAYER_HEAD, itemMeta -> {
                    itemMeta.setDisplayName("§a" + target.getName());
                }).build();
                items.add(item);
            }
        });
        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> buttons = new HashMap<>();
        buttons.put(48, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> itemMeta.setDisplayName("§cPrevious"))
                .setPreviousPageButton());
        buttons.put(49, new ItemBuilder(this, Material.BARRIER, itemMeta -> itemMeta.setDisplayName("§7Close"))
                .setCloseButton());
        buttons.put(50, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> itemMeta.setDisplayName("§aNext"))
                .setNextPageButton());
        return buttons;
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
                String playerName = ItemUtils.getItemDisplayName(clickedItem);
                Player target = Bukkit.getPlayer(playerName);
                if (target != null) {
                    TPACommand.sendTPARequest(getOwner(), target, plugin);
                    getOwner().closeInventory();
                }
            }
        }
    }
}
