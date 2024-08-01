package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.Map;

@Feature("TPA")
@Credit({"ddemile", "misieur", "process"})
public class TPACommand implements Listener {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    private final AywenCraftPlugin plugin;
    private final Map<Player, Player> pendingTeleport = new HashMap<>();

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") Player target) {
        if (target == null) {
            openPlayerSelectionMenu(player);
            return;
        }
        sendTeleportRequest(player, target);
    }

    private void sendTeleportRequest(Player player, Player target) {
        if (player == target) {
            player.sendMessage(Component.text("Vous ne pouvez pas vous téléporter à vous-même !").color(NamedTextColor.RED));
            return;
        }

        if (tpQueue.TPA_REQUESTS2.containsKey(player)) {
            player.sendMessage(Component.text("Vous avez déjà une demande de téléportation en attente...").color(NamedTextColor.RED));
            return;
        }

        tpQueue.TPA_REQUESTS.put(target, player);
        tpQueue.TPA_REQUESTS2.put(player, target);
        tpQueue.TPA_REQUESTS_TIME.put(player, System.currentTimeMillis() / 1000);

        player.sendMessage(Component.text("Vous avez envoyé une demande de tpa à " + target.getName()).color(NamedTextColor.GREEN));

        final TextComponent textComponent = Component.text(player.getName() + " vous a envoyé une demande de téléportation. Tapez ")
                .append(Component.text("/tpaccept").color(TextColor.color(0x00FF00)))
                .append(Component.text(" pour accepter."))
                .color(NamedTextColor.YELLOW)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(textComponent);

        new BukkitRunnable() {
            @Override
            public void run() {
                expireTpa(player, target);
            }
        }.runTaskLater(AywenCraftPlugin.getInstance(), 6000); // 6000 ticks = 5 minutes
    }

    private void expireTpa(Player player, Player target) {
        if (tpQueue.TPA_REQUESTS2.containsKey(player) && tpQueue.TPA_REQUESTS.get(target) == player) {
            player.sendMessage(Component.text("Votre demande de téléportation à " + target.getName() + " a expiré.").color(NamedTextColor.RED));
            target.sendMessage(Component.text("La demande de téléportation de " + player.getName() + " a expiré.").color(NamedTextColor.RED));
            tpQueue.TPA_REQUESTS.remove(target, player);
            tpQueue.TPA_REQUESTS2.remove(player, target);
            tpQueue.TPA_REQUESTS_TIME.remove(player);
        }
    }

    private void openPlayerSelectionMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("Sélectionnez un joueur").color(NamedTextColor.GOLD));
        int slot = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(player)) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = playerHead.getItemMeta();
                meta.setDisplayName(Component.text(target.getName()).color(NamedTextColor.YELLOW).toString());
                playerHead.setItemMeta(meta);
                inventory.setItem(slot++, playerHead);
            }
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getTitle().equals(Component.text("Sélectionnez un joueur").color(NamedTextColor.GOLD).toString())) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                Player target = Bukkit.getPlayer(clickedItem.getItemMeta().getDisplayName());
                if (target != null) {
                    player.closeInventory();
                    sendTeleportRequest(player, target);
                }
            }
        }
    }
}
