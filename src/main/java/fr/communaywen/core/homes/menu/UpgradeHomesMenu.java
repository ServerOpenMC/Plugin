package fr.communaywen.core.homes.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.homes.HomeUpgrade;
import fr.communaywen.core.homes.HomeUpgradeManager;
import fr.communaywen.core.homes.HomesManagers;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;

public class UpgradeHomesMenu extends Menu {

    private final HomeUpgradeManager upgradeManager;
    private final HomesManagers homesManagers;
    private final String TITLE;

    public UpgradeHomesMenu(Player owner, HomeUpgradeManager upgradeManager, HomesManagers homesManagers) {
        super(owner);
        this.upgradeManager = upgradeManager;
        this.homesManagers = homesManagers;
        TITLE = PlaceholderAPI.setPlaceholders(owner, "§r§f%img_offset_-8%%img_omc_homes_menus_home_upgrade%");
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> items = new HashMap<>();

        //int currentHomes = homesManagers.getHomeNamesByPlayer(getOwner().getUniqueId()).size();

        int homesCount = homesManagers.getHomeNamesByPlayer(getOwner().getUniqueId()).size();
        int currentHomes = 0;

        for(HomeUpgrade upgrade : HomeUpgrade.values()) {
            if(upgradeManager.getCurrentUpgrade(getOwner()).equals(upgrade)) {
                currentHomes = upgrade.getHomes();
            }
        }

        HomeUpgrade lastUpgrade = HomeUpgrade.valueOf("UPGRADE_" + HomeUpgrade.values().length);

        HomeUpgrade nextUpgrade = upgradeManager.getNextUpgrade(currentHomes) != null
                ? upgradeManager.getNextUpgrade(currentHomes)
                : lastUpgrade;

        int finalCurrentHomes = currentHomes;
        items.put(4, new ItemBuilder(this, CustomStack.getInstance("omc_homes:omc_homes_icon_upgrade").getItemStack(), itemMeta -> {
            itemMeta.setDisplayName("§8● §6Améliorer les homes §8(Click gauche)");
            List<String> lore = new ArrayList<>();
            if (nextUpgrade.equals(lastUpgrade)) {
                lore.add("§cVous avez atteint le nombre maximum de homes");
                lore.add("§eNombre de home actuel: " + finalCurrentHomes);
            } else {
                lore.add("§6Nombre de home actuel: §e" + finalCurrentHomes);
                lore.add("§bPrix: §a" + nextUpgrade.getPrice() + "$");
                lore.add("§6Nombre de home au prochain niveau: §e" + nextUpgrade.getHomes());
                lore.add("§7→ Clique pour améliorer tes homes");
            }

            itemMeta.setLore(lore);
        }).setOnClick(event -> {
            upgradeManager.upgradeHomes(getOwner());
            getOwner().closeInventory();
        }));

        return items;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }
}
