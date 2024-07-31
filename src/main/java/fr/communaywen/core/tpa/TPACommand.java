package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Feature("TPA")
@Credit({"ddemile", "misieur", "process", "Axillity"})
public class TPACommand implements Listener {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;
    private final AywenCraftPlugin plugin;
    private static final String INVENTORY_TITLE = "Sélectionnez un joueur";

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") String targetName) {
        if (targetName == null || targetName.isEmpty()) {
            openPlayerListGUI(player);
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cLe joueur '" + targetName + "' n'est pas en ligne.");
            return;
        }

        sendTPARequest(player, target);
    }

    private void sendTPARequest(Player player, Player target) {
        if (player.equals(target)) {
            player.sendMessage("§cVous ne pouvez pas vous téléporter à vous-même.");
            return;
        }

        if (tpQueue.hasPendingRequest(player)) {
            player.sendMessage("§cVous avez déjà une demande de téléportation en attente...");
            return;
        }

        tpQueue.addRequest(player, target);
        player.sendMessage("§aDemande de téléportation envoyée à §f" + target.getName() + "§a.");

        final Component message = Component.text(player.getName() + " vous a envoyé une demande de téléportation. Tapez /tpaccept pour accepter.")
                .color(TextColor.color(255, 255, 255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                tpQueue.expireRequest(player, target);
            }
        }.runTaskLater(plugin, 2400);
    }

    private void openPlayerListGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, INVENTORY_TITLE);

        int index = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                ItemStack head = createPlayerHead(onlinePlayer);
                gui.setItem(index++, head);
            }
        }

        player.openInventory(gui);
    }

    private ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.setDisplayName("§a" + player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equals(INVENTORY_TITLE)) return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() != Material.PLAYER_HEAD) return;

        SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
        if (meta != null && meta.getOwningPlayer() != null) {
            Player target = meta.getOwningPlayer().getPlayer();
            if (target != null) {
                sendTPARequest(player, target);
                player.closeInventory();
            }
        }
    }
}

