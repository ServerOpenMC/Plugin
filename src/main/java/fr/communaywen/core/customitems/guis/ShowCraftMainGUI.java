package fr.communaywen.core.customitems.guis;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShowCraftMainGUI extends PaginatedMenu {

    public ShowCraftMainGUI(Player owner) {
        super(owner);
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.BLACK_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return StaticSlots.STANDARD;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        return List.of();
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {

        Map<Integer, ItemStack> content = new HashMap<>();
        ArrayList<ItemBuilder> navBtns = CustomItemsUtils.getNavigationButtons(this);

        content.put(48, navBtns.getFirst().setBackButton());
        content.put(49, navBtns.get(1).setCloseButton());
        content.put(50, navBtns.getLast().setNextPageButton());

        return content;
    }

    @Override
    public @NotNull String getName() {
        return "";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
