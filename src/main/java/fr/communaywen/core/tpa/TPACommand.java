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
@Credit({"ddemile", "Axillity", "misieur", "process"})
public class TPACommand implements Listener {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;
    private final AywenCraftPlugin plugin;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") String targetName) {
        if (targetName == null || targetName.isEmpty()) {
            new TPAMenu(player).open();
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cLe joueur '" + targetName + "' n'est pas en ligne.");
            return;
        }

        sendTPARequest(player, target);
    }

    public static void sendTPARequest(Player player, Player target) {
        TPAQueue tpQueue = TPAQueue.INSTANCE;
        AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();

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
                .color(TextColor.color(30, 100, 150))
                .clickEvent(ClickEvent.runCommand("/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                tpQueue.expireRequest(player, target);
            }
        }.runTaskLater(plugin, 2400);
    }
}
