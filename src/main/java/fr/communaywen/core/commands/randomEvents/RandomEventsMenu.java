package fr.communaywen.core.commands.randomEvents;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.EventsManager;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.List;


public class RandomEventsMenu extends Menu {

    private EventsManager eventsManager;
    private boolean isAdmin;

    public RandomEventsMenu(Player player, AywenCraftPlugin plugin) {
        super(player);

        eventsManager = plugin.eventsManager;
        isAdmin = player.isOp();
    }

    @Override
    public @NotNull String getName() {
        if (isAdmin) {
            return ChatColor.GOLD + "Events aléatoires - Admin";
        } else {
            return ChatColor.BLACK + "Events aléatoires - Paramètres";
        }        
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = fill(Material.GRAY_STAINED_GLASS_PANE);

        ItemBuilder item;
            ItemMeta meta;

        if (isAdmin) {            
            item = new ItemBuilder(this, Material.ZOMBIE_SPAWN_EGG);
            meta = item.getItemMeta();
            meta.itemName(Component.text("Nuit Terrifiante"));
            
            if (eventsManager.isTerrifyingNightEnabled()) {
                meta.lore(List.of(
                    Component.text(ChatColor.GREEN + "Activé"),
                    Component.text("Clic gauche pour désactiver"),
                    Component.text(ChatColor.GOLD + "Clic droit pour lancer")
                ));
                meta.setEnchantmentGlintOverride(true);
                item.setOnClick(event -> {
                    if (event.isRightClick()) {
                        eventsManager.startTerrifyingNight();
                    } else {
                        eventsManager.enabledEvents.remove(EventsManager.TERRIFYING_NIGHT);
                    }
                    open();
                });
            } else {
                meta.lore(List.of(
                    Component.text(ChatColor.RED + "Désactivé"),
                    Component.text("Clic gauche pour réactiver"),
                    Component.text(ChatColor.GOLD + "Clic droit pour lancer")
                ));
                item.setOnClick(event -> {
                    if (event.isRightClick()) {
                        eventsManager.startTerrifyingNight();
                    } else {
                        eventsManager.enabledEvents.add(EventsManager.TERRIFYING_NIGHT);
                    }
                    open();
                });
            }
            item.setItemMeta(meta);
            content.put(10, item);


            item = new ItemBuilder(this, Material.FISHING_ROD);
            meta = item.getItemMeta();
            meta.itemName(Component.text("Pêche Miraculeuse"));

            if (eventsManager.isMiraculousFishingEnabled()) {
                meta.lore(List.of(
                    Component.text(ChatColor.GREEN + "Activé"),
                    Component.text(ChatColor.RED + "Clic gauche pour désactiver"),
                    Component.text(ChatColor.GOLD + "Clic droit pour lancer")
                ));
                meta.setEnchantmentGlintOverride(true);
                item.setOnClick(event -> {
                    if (event.isRightClick()) {
                        eventsManager.startMiraculousFishing();
                    } else {
                        eventsManager.enabledEvents.remove(EventsManager.MIRACULOUS_FISHING);
                    }
                    open();
                });
            } else {
                meta.lore(List.of(
                    Component.text(ChatColor.RED + "Désactivé"),
                    Component.text(ChatColor.GREEN + "Clic gauche pour réactiver"),
                    Component.text(ChatColor.GOLD + "Clic droit pour lancer")
                ));
                item.setOnClick(event -> {
                    if (event.isRightClick()) {
                        eventsManager.startMiraculousFishing();
                    } else {
                        eventsManager.enabledEvents.add(EventsManager.MIRACULOUS_FISHING);
                    }
                    open();
                });
            }
            item.setItemMeta(meta);
            content.put(11, item);


            if (eventsManager.isRunning()) {
                item = new ItemBuilder(this, Material.GREEN_WOOL);
                meta = item.getItemMeta();
                meta.itemName(Component.text(ChatColor.RED + "Désactiver les events"));
                meta.setEnchantmentGlintOverride(true);
                item.setOnClick(event -> {
                    eventsManager.stop();
                    open();
                });
            } else {
                item = new ItemBuilder(this, Material.RED_WOOL);
                meta = item.getItemMeta();
                meta.itemName(Component.text(ChatColor.GREEN + "Réactiver les events"));
                item.setOnClick(event -> {
                    eventsManager.start();
                    open();
                });
            }
            item.setItemMeta(meta);
            content.put(15, item);

            
            item = new ItemBuilder(this, Material.PLAYER_HEAD);
            meta = item.getItemMeta();
            meta.itemName(Component.text(ChatColor.GOLD + "Menu joueur"));
            item.setItemMeta(meta);

            item.setOnClick(event -> {
                isAdmin = false;
                open();
            });
            content.put(16, item);

        } else {

            Integer difficulty = RandomEventsData.getPlayerDifficulty(getOwner());
            

            switch (difficulty) {
                case EventsDifficulties.DISABLED:
                    item = new ItemBuilder(this, Material.BARRIER);
                    meta = item.getItemMeta();
                    
                    meta.lore(List.of(
                        Component.text(ChatColor.RED + "Désactivé"),
                        Component.text("Clic gauche pour changer la difficulté")
                    ));
                    item.setOnClick(event -> {
                        RandomEventsData.setPlayerDifficulty(getOwner(), EventsDifficulties.EASY);
                        open();
                    });
                    break;

                case EventsDifficulties.EASY:
                    item = new ItemBuilder(this, Material.ZOMBIE_SPAWN_EGG);
                    meta = item.getItemMeta();

                    meta.lore(List.of(
                        Component.text(ChatColor.GREEN + "Facile"),
                        Component.text("Clic gauche pour changer la difficulté")
                    ));
                    item.setOnClick(event -> {
                        RandomEventsData.setPlayerDifficulty(getOwner(), EventsDifficulties.NORMAL);
                        open();
                    });
                    break;

                case EventsDifficulties.NORMAL:
                    item = new ItemBuilder(this, Material.ZOMBIE_HEAD);
                    meta = item.getItemMeta();

                    meta.lore(List.of(
                        Component.text(ChatColor.YELLOW + "Normale"),
                        Component.text("Clic gauche pour changer la difficulté")
                    ));
                    item.setOnClick(event -> {
                        RandomEventsData.setPlayerDifficulty(getOwner(), EventsDifficulties.HARD);
                        open();
                    });
                    break;

                case EventsDifficulties.HARD:
                    item = new ItemBuilder(this, Material.WITHER_SKELETON_SKULL);
                    meta = item.getItemMeta();

                    meta.lore(List.of(
                        Component.text(ChatColor.LIGHT_PURPLE + "Difficile"),
                        Component.text("Clic gauche pour changer la difficulté")
                    ));
                    item.setOnClick(event -> {
                        RandomEventsData.setPlayerDifficulty(getOwner(), EventsDifficulties.DISABLED);
                        open();
                    });
                    break;
            
                default:
                    // Achievement unlocked: How did we get there ?
                    item = new ItemBuilder(this, Material.AIR);
                    meta = item.getItemMeta();
                    break;
            }
            meta.itemName(Component.text(ChatColor.GOLD + "Difficulté des events"));
            item.setItemMeta(meta);
            content.put(13, item);
        }
        return content;
    }
}