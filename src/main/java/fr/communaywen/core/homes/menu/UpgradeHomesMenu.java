package fr.communaywen.core.homes.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.communaywen.core.homes.HomeUpgrade;
import fr.communaywen.core.homes.HomeUpgradeManager;
import fr.communaywen.core.homes.HomesManagers;
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

    public UpgradeHomesMenu(Player owner, HomeUpgradeManager upgradeManager, HomesManagers homesManagers) {
        super(owner);
        this.upgradeManager = upgradeManager;
        this.homesManagers = homesManagers;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> items = new HashMap<>();

        int[] blueSlots = { 0, 1, 7, 8, 9, 17, 18, 19, 25, 26 };
        int[] lightBlueSlots = { 2, 3, 5, 6, 10, 11, 15, 16, 20, 21, 23, 24 };
        int[] blackSlots = { 4, 12, 14, 22 };

        for (int slot : blueSlots) {
            items.put(slot,
                    new ItemBuilder(this, Material.BLUE_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }
        for (int slot : lightBlueSlots) {
            items.put(slot, new ItemBuilder(this, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    itemMeta -> itemMeta.setDisplayName(" ")));
        }
        for (int slot : blackSlots) {
            items.put(slot,
                    new ItemBuilder(this, Material.BLACK_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        int currentHomes = homesManagers.getHomeNamesByPlayer(getOwner().getUniqueId()).size();

        HomeUpgrade lastUpgrade = HomeUpgrade.valueOf("UPGRADE_" + HomeUpgrade.values().length);

        HomeUpgrade nextUpgrade = upgradeManager.getNextUpgrade(currentHomes) != null
                ? upgradeManager.getNextUpgrade(currentHomes)
                : lastUpgrade;

        items.put(13, new ItemBuilder(this, Material.EXPERIENCE_BOTTLE, itemMeta -> {
            itemMeta.setDisplayName("§8● §6Améliorer les homes §8(Click gauche)");
            List<String> lore = new ArrayList<>();
            if (nextUpgrade.equals(lastUpgrade)) {
                lore.add("§cVous avez atteint le nombre maximum de homes");
                lore.add("§eNombre de home actuel: " + currentHomes);
            } else {
                lore.add("§6Nombre de home actuel: §e" + currentHomes);
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
        return InventorySize.NORMAL;
    }

    @Override
    public @NotNull String getName() {
        return "§6§lHomes §8§l» §e§lUpgrade";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }
}
