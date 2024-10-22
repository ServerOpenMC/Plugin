package fr.communaywen.core.adminshop.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.adminshop.AdminShopCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AdminShopMenu extends Menu {

    public AdminShopMenu(Player player) {
        super(player);
    }

    @Override
    public @NotNull String getName() {
        return "§6Admin Shop";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            content.put(i, new ItemBuilder(this, Material.GRAY_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        int slot = 10;
        for(AdminShopCategory category : AdminShopCategory.values()) {

            content.put(slot, new ItemBuilder(this, category.getBlocks(), itemMeta -> {
                itemMeta.setDisplayName(category.getName());
                itemMeta.setLore(Arrays.asList("  §8■ §7Click gauche pour ouvrir la catégorie"));
            }).setNextMenu(category.createMenu(getOwner())));


            if(slot != 16) slot += 2;
        }

        return content;
    }
}