package fr.communaywen.core.tpa;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import fr.communaywen.core.tpa.TPACommandGUI;

@Feature("TPA")
@Credit({"ddemile", "Axillity", "misieur", "process"})
public class TPACommand implements Listener {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;
    private final AywenCraftPlugin plugin;
    private static final int PLAYERS_PER_PAGE = 45;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") String targetName) {
        if (targetName == null || targetName.trim().isEmpty()) {
            new TPACommandGUI(player, plugin).open();
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(Component.text("[TPA] ❌ Le joueur '" + targetName + "' n'est pas en ligne.")
                    .color(TextColor.color(255, 0, 0)));
            return;
        }

        sendTPARequest(player, target, plugin);
    }

    public static void sendTPARequest(Player player, Player target, AywenCraftPlugin plugin) {
        if (player.equals(target)) {
            player.sendMessage(Component.text("[TPA] ❌ Vous ne pouvez pas vous téléporter à vous-même.")
                    .color(TextColor.color(255, 0, 0)));
            return;
        }

        if (TPAQueue.INSTANCE.hasPendingRequest(player)) {
            player.sendMessage(Component.text("[TPA] ❌ Vous avez déjà une demande de téléportation en attente...")
                    .color(TextColor.color(255, 0, 0)));
            return;
        }

        TPAQueue.INSTANCE.addRequest(player, target);
        player.sendMessage(Component.text("[TPA] ✅ Demande de téléportation envoyée à ")
                .color(TextColor.color(0, 255, 0))
                .append(Component.text(target.getName())
                        .color(TextColor.color(0, 255, 255))) // Light blue color for the target's name
                .append(Component.text(" ✅"))
                .color(TextColor.color(0, 255, 0)));

        final Component message = Component.text(player.getName() + " vous a envoyé une demande de téléportation. Tapez /tpaccept pour accepter.")
                .color(TextColor.color(0, 255, 255)) // Light blue color for the neutral text
                .clickEvent(ClickEvent.runCommand("/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("[TPA] §7[§aCliquez pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                TPAQueue.INSTANCE.expireRequest(player, target);
            }
        }.runTaskLater(plugin, 2400);
    }
}
