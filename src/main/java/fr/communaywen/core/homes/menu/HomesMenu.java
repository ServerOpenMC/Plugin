package fr.communaywen.core.homes.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomeUpgradeManager;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.homes.menu.settings.HomeSettingsMenu;
import fr.communaywen.core.homes.menu.utils.HomeMenuUtils;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import me.clip.placeholderapi.PlaceholderAPI;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import net.md_5.bungee.api.ChatColor;

public class HomesMenu extends PaginatedMenu {
    
    private final List<Home> homes;
    private final HomeUpgradeManager upgradeManager;
    private final HomesManagers homesManagers;
    private final boolean wasTarget;
    private final String TITLE;

    public HomesMenu(Player owner, List<Home> homes, HomeUpgradeManager upgradeManager, boolean wasTarget) {
        super(owner);
        this.homes = homes;
        this.homesManagers = AywenCraftPlugin.getInstance().getManagers().getHomesManagers();
        this.upgradeManager = upgradeManager;
        this.wasTarget = wasTarget;
        TITLE = PlaceholderAPI.setPlaceholders(owner, "§r§f%img_offset_-8%%img_omc_homes_menus_home%");
    }

    @Override
    public Material getBorderMaterial() {
        return Material.BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public List<Integer> getStaticSlots() {
        List<Integer> staticSlots = new ArrayList<>();
        staticSlots.add(45);
        staticSlots.add(48);
        staticSlots.add(49);
        staticSlots.add(50);
        staticSlots.add(53);

        return staticSlots;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (Home home : homes) {
            items.add(new ItemBuilder(this, home.getCustomIcon(), itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + home.getName());
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ §aClique §2gauche pour vous téléporter",
                        ChatColor.GRAY + "■ §cCliquez §4droit §cpour configurer le home"
                ));
            }).setOnClick(event -> {
                MenuLib.setLastMenu(getOwner(), this);

                if (event.getClick() == ClickType.LEFT) {
                    getOwner().teleport(home.getLocation());
                    MessageManager.sendMessageType(getOwner(), "Téléportation à votre home: " + home.getName() + "...", Prefix.HOME, MessageType.SUCCESS, true);
                    getOwner().playSound(getOwner().getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                } else {
                    HomeSettingsMenu settingsMenu = new HomeSettingsMenu(
                            getOwner(),
                            home
                    );

                    settingsMenu.open();
                }
            }));
        }

        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> map = new HashMap<>();

        if(!wasTarget) {
            map.put(45, new ItemBuilder(this, CustomStack.getInstance("omc_homes:omc_homes_icon_information").getItemStack(),
                    itemMeta -> {
                        itemMeta.setDisplayName("§8(§bⓘ§8) §6Informations sur vos homes");
                        itemMeta.setLore(List.of(
                                "§6Chaque icon qui représente un home et lié au nom du home, par exemple, si vous appelé votre home 'maison', l'icone sera 'maison'",
                                "§7",
                                "§6Vous pouvez configurer le home en effectuant un clique droit sur l'icone du home",
                                "§6Vous pouvez vous téléporter à votre home en effectuant un clique gauche sur l'icone du home"
                        ));
                    }
                )
            );

            map.put(53, new ItemBuilder(this, CustomStack.getInstance("omc_homes:omc_homes_icon_upgrade").getItemStack(), itemMeta -> {
                itemMeta.setDisplayName("§8● §6Améliorer les homes §8(Click ici)");
                itemMeta.setLore(List.of(
                        "§6Cliquez pour améliorer vos homes"
                ));
            }).setOnClick(event -> {
                UpgradeHomesMenu upgradeHomesMenu = new UpgradeHomesMenu(getOwner(), upgradeManager, homesManagers);
                upgradeHomesMenu.open();
            }));
        }

        map.put(48, new ItemBuilder(this, MailboxMenuManager.previousPageBtn()).setPreviousPageButton());
        map.put(49, new ItemBuilder(this, MailboxMenuManager.cancelBtn()).setCloseButton());
        map.put(50, new ItemBuilder(this, MailboxMenuManager.nextPageBtn()).setNextPageButton());

        return map;
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
    }

}
