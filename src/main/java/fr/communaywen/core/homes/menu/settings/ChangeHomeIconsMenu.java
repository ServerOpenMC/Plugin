package fr.communaywen.core.homes.menu.settings;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.homes.menu.UpgradeHomesMenu;
import fr.communaywen.core.homes.menu.utils.HomeIcons;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import me.clip.placeholderapi.PlaceholderAPI;
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

public class ChangeHomeIconsMenu extends PaginatedMenu {

    private final Home home;
    private final String TITLE;

    public ChangeHomeIconsMenu(Player owner, Home home) {
        super(owner);
        this.home = home;
        TITLE = PlaceholderAPI.setPlaceholders(owner, "§r§f%img_offset_-8%%img_omc_homes_menus_home%");
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        List<Integer> staticSlots = new ArrayList<>();
        staticSlots.add(45);
        staticSlots.add(48);
        staticSlots.add(49);
        staticSlots.add(50);
        staticSlots.add(53);

        return staticSlots;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for(int i = 0; i < HomeIcons.values().length; i++) {
            HomeIcons homeIcon = HomeIcons.values()[i];
            items.add(new ItemBuilder(this, CustomStack.getInstance(homeIcon.getId()).getItemStack()).setOnClick(event -> {
                AywenCraftPlugin.getInstance().getManagers().getHomesManagers().changeIcon(home, homeIcon);
                getOwner().closeInventory();
            }));
        }

        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> map = new HashMap<>();

        map.put(45, new ItemBuilder(this, CustomStack.getInstance("_iainternal:icon_back_orange").getItemStack(), itemMeta -> itemMeta.setDisplayName("§7Retour")).setBackButton());
        map.put(48, new ItemBuilder(this, MailboxMenuManager.previousPageBtn()).setPreviousPageButton());
        map.put(49, new ItemBuilder(this, MailboxMenuManager.cancelBtn()).setCloseButton());
        map.put(50, new ItemBuilder(this, MailboxMenuManager.nextPageBtn()).setNextPageButton());
        map.put(53, new ItemBuilder(this, CustomStack.getInstance("omc_homes:omc_homes_icon_upgrade").getItemStack(), itemMeta -> {
            itemMeta.setDisplayName("§aSauvegarder");
            itemMeta.setLore(List.of(
                    "§6Cliquez pour sauvegarder l'icone de votre home"
            ));
        }).setOnClick(event -> {
            getOwner().closeInventory();
        }));

        return map;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}
}
