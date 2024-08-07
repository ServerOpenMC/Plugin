package fr.communaywen.core.tpa;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.AywenCraftPlugin;
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

public class TPAMenu extends PaginatedMenu {

    private final AywenCraftPlugin plugin;
    private final TPAQueue tpQueue;

    public TPAMenu(Player owner, AywenCraftPlugin plugin) {
        super(owner);
        this.plugin = plugin;
        this.tpQueue = TPAQueue.INSTANCE;
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
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(getOwner())) {
                items.add(new ItemBuilder(this, ItemUtils.getPlayerSkull(onlinePlayer.getUniqueId()), itemMeta -> {
                    itemMeta.setDisplayName(ChatColor.GREEN + onlinePlayer.getName());
                    itemMeta.setLore(List.of(
                            ChatColor.GRAY + "Cliquez pour envoyer une demande de téléportation"
                    ));
                }).setOnClick(event -> {
                    TPACommand.sendTPARequest(getOwner(), onlinePlayer, plugin);
                    getOwner().closeInventory();
                }));
            }
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
        return "Liste des joueurs";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
    }
}
