package fr.communaywen.core.homes.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import fr.communaywen.core.homes.Home;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import net.md_5.bungee.api.ChatColor;

public class HomesMenu extends PaginatedMenu {
    
    private final List<Home> homes;
    private final String playerName;

    public HomesMenu(Player owner, List<Home> homes, String playerName) {
        super(owner);
        this.homes = homes;
        this.playerName = playerName;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public List<Integer> getStaticSlots() {
        return StaticSlots.STANDARD;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < homes.size(); i++) {
            int finalI = i;
            items.add(new ItemBuilder(this, Material.GRASS_BLOCK, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + homes.get(finalI).getName());
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ Cliquez pour vous téléporter"
                ));
            }).setOnClick(event -> {
                getOwner().teleport(homes.get(finalI).getLocation());
                MessageManager.sendMessageType(getOwner(), "Téléportation à votre home: " + homes.get(finalI).getName() + "...", Prefix.HOME, MessageType.SUCCESS, true);
                getOwner().playSound(getOwner().getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            }));
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
        return "Liste des homes de " + playerName;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
    }

}
