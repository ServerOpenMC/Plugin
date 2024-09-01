package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.ProgressBar;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.Arrays;
import java.util.Objects;

import static fr.communaywen.core.utils.ProgressBar.createProgressBar;

public class RocketMenu implements Listener {

    private static final int INVENTORY_SIZE = 54;
    private static final int COAL_SLOT = 13;

    private static final int BUTTON_SLOT = 40;
    private final Rocket rocket;

    @Getter
    private final Inventory inventory;

    public RocketMenu(Rocket rocket) {
        JavaPlugin plugin = AywenCraftPlugin.getInstance();
        this.rocket = rocket;
        String title = PlaceholderAPI.setPlaceholders(null, "§r§f%img_offset_-8%%img_rocket_menu%");
        this.inventory = Bukkit.createInventory(null, INVENTORY_SIZE, title);
        initializeInventory();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void initializeInventory() {
        ItemStack invisibleBtn = CustomStack.getInstance("space:invisiblebtn").getItemStack();
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (i == COAL_SLOT) {
                inventory.setItem(i, createCoalCounterItem());
            } else if ((i >= BUTTON_SLOT-3 && i <= BUTTON_SLOT+3) || i >= BUTTON_SLOT-12 && i <= BUTTON_SLOT-6) {
                inventory.setItem(i, i == BUTTON_SLOT ? createLaunchButton("button") : createLaunchButton("invisible"));
            } else {
                    inventory.setItem(i, invisibleBtn);
            }
        }
    }
    private ItemStack createCoalCounterItem() {
        ItemStack coalCounter = CustomStack.getInstance("space:invisiblebtn").getItemStack();
        ItemMeta meta = coalCounter.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(rocket.getCoalCount() >= 1024 ? "§r§aRéservoir plein" : "§r§7§oDéposez le charbon ici");
            meta.setLore(Arrays.asList("§r", " "+createProgressBar((double) rocket.getCoalCount() /1024*100, 20, "§a") + " §r§f" + rocket.getCoalCount() + "/1024", "§r"));
            coalCounter.setItemMeta(meta);
        }
        return coalCounter;
    }

    private ItemStack createLaunchButton(String type) {
        boolean canLaunch = rocket.getCoalCount() >= 1024;
        ItemStack button = type.equals("button") ? (canLaunch ? CustomStack.getInstance("space:launchbtn").getItemStack() : CustomStack.getInstance("space:launchbtndisabled").getItemStack()) : CustomStack.getInstance("space:invisiblebtn").getItemStack();
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(canLaunch ? "§r§aDémarrer la fusée" : "§r§7Démarrer la fusée");
            if(!canLaunch) meta.setLore(Arrays.asList("§r", "§r§cIl n'y a pas assez de charbon dans la fusée pour démarrer", "§r"));
            button.setItemMeta(meta);
        }
        return button;
    }

    public void openMenu(Player player) {
        if(rocket.isLaunched()) {
            MessageManager.sendMessageType(player, "La fusée est déjà partie.", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }
        if(!inventory.getViewers().isEmpty()) {
            MessageManager.sendMessageType(player, "Un autre joueur est déjà dans cette fusée.", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getInventory() != inventory) return;

        int slot = event.getRawSlot();

        if (slot >= 0 && slot < INVENTORY_SIZE) {
            if (slot == COAL_SLOT) {
                handleCoalSlotClick(event, player);
            } else {
                event.setCancelled(true);
            }
            if((slot >= BUTTON_SLOT-3 && slot <= BUTTON_SLOT+3) || slot >= BUTTON_SLOT-12 && slot <= BUTTON_SLOT-6) {
                rocket.launch(player);
            }
        } else if (event.isShiftClick() && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.COAL) {
            event.setCancelled(true);
            handleCoalAddition(event.getCurrentItem(), player);
        }
    }

    private void handleCoalSlotClick(InventoryClickEvent event, Player player) {
        ItemStack cursorItem = event.getCursor();
        if (cursorItem != null && cursorItem.getType() == Material.COAL) {
            event.setCancelled(true);
            handleCoalAddition(cursorItem, player);
        } else {
            event.setCancelled(true);
        }
    }

    private void handleCoalAddition(ItemStack coalItem, Player player) {
        int coalToAdd = coalItem.getAmount();
        if (rocket.getCoalCount() + coalToAdd > 1024) {
            coalToAdd = 1024 - rocket.getCoalCount();
            MessageManager.sendMessageType(player, "Le réservoir de la fusée est plein !", Prefix.SPACE, MessageType.INFO, false);
        }
        rocket.addCoalCount(coalToAdd);
        coalItem.setAmount(coalItem.getAmount() - coalToAdd);
        inventory.setItem(COAL_SLOT, createCoalCounterItem());


        for (int i = BUTTON_SLOT-3; i <= BUTTON_SLOT+3; i++) {
            inventory.setItem(i, createLaunchButton(i == BUTTON_SLOT ? "button" : "invisible"));
        }

        for (int i = BUTTON_SLOT-12; i <= BUTTON_SLOT-6; i++) {
            inventory.setItem(i, createLaunchButton("invisible"));
        }



//        MessageManager.sendMessageType(player, "Charbon ajouté, total :" + rocket.getCoalCount(), Prefix.SPACE, MessageType.SUCCESS, false);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory() != inventory) return;

        for (int slot : event.getRawSlots()) {
            if (slot >= 0 && slot < INVENTORY_SIZE && slot != COAL_SLOT) {
                event.setCancelled(true);
                return;
            }
        }
    }
}