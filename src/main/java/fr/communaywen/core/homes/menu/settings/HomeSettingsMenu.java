package fr.communaywen.core.homes.menu.settings;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.homes.menu.HomesMenu;
import fr.communaywen.core.homes.menu.utils.HomeMenuUtils;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeSettingsMenu extends Menu {

    private static final String TITLE = PlaceholderAPI.setPlaceholders(null, "§r§f%img_offset_-8%%img_omc_homes_menus_home_settings%");
    private final Home home;

    public HomeSettingsMenu(Player owner, Home home) {
        super(owner);
        this.home = home;
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGER;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        content.put(4, home.getCustomIcon());

        content.put(20, new ItemBuilder(this, HomeMenuUtils.getRandomsIcons(), itemMeta -> {
            itemMeta.setDisplayName("§aChanger l'icône");
            itemMeta.setLore(List.of(ChatColor.GRAY + "■ §aClique §2gauche §apour changer l'icône de votre home"));
        }).setNextMenu(new ChangeHomeIconsMenu(getOwner(), home)));

        content.put(24, new ItemBuilder(this, CustomStack.getInstance("omc_homes:omc_homes_icon_bin_red").getItemStack(), itemMeta -> {
            itemMeta.setDisplayName(new FontImageWrapper("omc_homes:bin").getString() + " §cSupprimer le home");
            itemMeta.setLore(List.of(ChatColor.GRAY + "■ §cClique §4gauche §cpour supprimer votre home"));
        }).setNextMenu(new DeleteHomeConfirmationMenu(getOwner(), home)));

        content.put(36, new ItemBuilder(this, MailboxMenuManager.previousPageBtn()).setOnClick(event -> {
            new HomesMenu(getOwner(),
                    HomesManagers.getHomes(getOwner().getUniqueId()),
                    AywenCraftPlugin.getInstance().getManagers().getHomeUpgradeManager(),
                    false
            ).open();
        }));

        content.put(44, new ItemBuilder(this, MailboxMenuManager.cancelBtn()).setCloseButton());

        return content;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}
}
