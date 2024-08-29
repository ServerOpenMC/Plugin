package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("TPA")
@Credit({"Axillity", "ddemile", "misieur", "process"})
public class TPACommand implements Listener {

    private final AywenCraftPlugin plugin;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") String targetName) {
        if (targetName == null || targetName.trim().isEmpty()) {
            player.sendMessage(Component.text("[TPA] ❌ Vous devez spécifier un joueur.")
                    .color(TextColor.color(255, 0, 0)));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(Component.text("[TPA] ❌ Le joueur '" + targetName + "' n'est pas en ligne.")
                    .color(TextColor.color(255, 0, 0)));
            return;
        }

        if (target.getWorld().getName().equals("dreamworld")) {
            player.sendMessage("Vous ne pouvez pas téléporter quelq'un dans la dimension");
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
                        .color(TextColor.color(0, 255, 255)))
                .append(Component.text(" ✅"))
                .color(TextColor.color(0, 255, 0)));

        final Component message = Component.text(player.getName() + " vous a envoyé une demande de téléportation. Tapez /tpaccept pour accepter.")
                .color(TextColor.color(0, 255, 255))
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
