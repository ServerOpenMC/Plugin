package fr.communaywen.core.warp;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.arena.ArenaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpMenu extends Menu {
    public WarpMenu(Player owner) {
        super(owner);
    }

    @Override
    public @NotNull String getName() {
        return "Le Globe des Warps";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGE;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent click) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        List<String> lore_globe = new ArrayList<String>();
        lore_globe.add("§7Vous permet de naviguer entre des §6§lWaypoints");
        lore_globe.add("");
        lore_globe.add("§8§oCredit à Seiny");

        inventory.put(4, new ItemBuilder(this, Material.BLUE_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§9§lLe Globe");
            itemMeta.setLore(lore_globe);
        }));

        List<String> lore_soon = new ArrayList<String>();
        lore_soon.add("§csoon...");

        inventory.put(10, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(11, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(12, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        List<String> lore_pvp = new ArrayList<String>();
        lore_pvp.add("§7Aller à l'§cArène PVP Basique");
        inventory.put(13, new ItemBuilder(this, Material.IRON_SWORD, itemMeta -> {
            itemMeta.setDisplayName("§c§lL'Arène PVP");
            itemMeta.setLore(lore_pvp);
        }).setOnClick(inventoryClickEvent -> {
            ArenaManager.tpPlayerOnRandomPosition("arena_pvp", getOwner());
        }));

        //add a slot with Contest Arena
        inventory.put(14, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(15, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(16, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(21, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(22, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        inventory.put(23, new ItemBuilder(this, Material.RED_STAINED_GLASS, itemMeta -> {
            itemMeta.setDisplayName("§csoon...");
            itemMeta.setLore(lore_soon);
        }));

        return inventory;
    }
}
