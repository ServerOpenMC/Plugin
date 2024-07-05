package fr.communaywen.core.teams.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class ConfirmationMenu extends Menu {

    private final Consumer<InventoryClickEvent> toConfirm;

    public ConfirmationMenu(Player owner, Consumer<InventoryClickEvent> toConfirm) {
        super(owner);
        this.toConfirm = toConfirm;
    }

    @Override
    public @NotNull String getName() {
        return ChatColor.GOLD + "Confirmation";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = fill(Material.GRAY_STAINED_GLASS_PANE);
        content.put(2, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Confirmer");
        }).setOnClick(toConfirm));
        content.put(6, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Annuler");
        }).setBackButton());
        return content;
    }
}
