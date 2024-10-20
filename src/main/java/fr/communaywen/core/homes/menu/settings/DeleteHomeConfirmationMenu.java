package fr.communaywen.core.homes.menu.settings;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.homes.menu.utils.HomeMenuUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteHomeConfirmationMenu extends Menu {

    private final Home home;
    private final HomesManagers homesManagers;
    private final String TITLE;

    public DeleteHomeConfirmationMenu(Player owner, Home home) {
        super(owner);
        this.home = home;
        this.homesManagers = AywenCraftPlugin.getInstance().getManagers().getHomesManagers();
        TITLE = PlaceholderAPI.setPlaceholders(owner, "§r§f%img_offset_-8%%img_omc_homes_menus_home_delete%");
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        content.put(2, new ItemBuilder(
                this,
                CustomStack.getInstance("omc_homes:omc_homes_icon_bin_red").getItemStack(),
                itemMeta ->
                        itemMeta.setDisplayName("§cConfirmer la suppression")
                ).setOnClick(event -> {
                    homesManagers.removeHome(home);
                    MessageManager.sendMessageType(getOwner(), "§aHome §e" + home.getName() + " §asupprimé avec succès !", Prefix.HOME, MessageType.SUCCESS, true);
                    getOwner().closeInventory();
                })
        );

        content.put(4, new ItemBuilder(
                this,
                HomeMenuUtils.getHomeButton(home),
                itemMeta -> {
                    itemMeta.setDisplayName("§a" + home.getName());
                    itemMeta.setLore(List.of(
                            ChatColor.GRAY + "■ §cClique §4gauche §cpour confirmer la suppression"
                    ));
                }
        ).setOnClick(event -> {
            homesManagers.removeHome(home);
            MessageManager.sendMessageType(getOwner(), "§aHome §4" + home.getName() + " §asupprimé avec succès !", Prefix.HOME, MessageType.SUCCESS, true);
            getOwner().closeInventory();
        }));

        content.put(6, new ItemBuilder(
                this,
                CustomStack.getInstance("omc_homes:omc_homes_icon_bin").getItemStack(),
                itemMeta ->
                        itemMeta.setDisplayName("§aAnnuler la suppression")
                ).setBackButton()
        );

        return content;
    }
}
