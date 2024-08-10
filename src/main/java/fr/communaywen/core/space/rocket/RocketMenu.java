package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RocketMenu extends Menu {

    private final CustomEntity rocket;

    public RocketMenu(Player owner, CustomEntity rocket) {
        super(owner);
        this.rocket = rocket;
    }

    @Override
    public @NotNull String getName() {
        return PlaceholderAPI.setPlaceholders(getOwner(), "§r§f%img_offset_-9%%img_rocket_menu%");
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.getWhoClicked().sendMessage("You clicked on the rocket menu");
        inventoryClickEvent.setCancelled(false);
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        //Map<Integer, ItemStack> content = fillCustomStack(CustomStack.getInstance("space:invisiblebtn"));

        Map<Integer, ItemStack> content = fill(Material.LIGHT_BLUE_CONCRETE);

        content.remove(16);

        return content;
    }

    public final Map<Integer, ItemStack> fillCustomStack(CustomStack customStack) {
        Map<Integer, ItemStack> map = new HashMap();

        for(int i = 0; i < this.getInventorySize().getSize(); ++i) {
            ItemStack filler = customStack.getItemStack();
            map.put(i, filler);
        }

        return map;
    }
}