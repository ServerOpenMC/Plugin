package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.spawn.jump.JumpManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
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
    public void onCommand(Player player, Player target) {
        String targetName = target.getName();
        if (targetName == null || targetName.trim().isEmpty()) {
            MessageManager.sendMessageType(player, "§cVous devez spécifier un joueur.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if (target == null) {
            MessageManager.sendMessageType(player, "§cLe joueur '" + targetName + "' n'est pas en ligne.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if (target.getWorld().getName().equals("dreamworld")) {
            MessageManager.sendMessageType(player, "§cVous ne pouvez pas téléporter quelqu'un dans la dimension", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        sendTPARequest(player, target, plugin);
    }

    public static void sendTPARequest(Player player, Player target, AywenCraftPlugin plugin) {
        if (player.equals(target)) {
            MessageManager.sendMessageType(player, "§cVous ne pouvez pas vous téléporter à vous-même.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if (TPAQueue.INSTANCE.hasPendingRequest(player)) {
            MessageManager.sendMessageType(player, "§cVous avez déjà une demande de téléportation en attente...", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if(JumpManager.isJumping(target)) {
            MessageManager.sendMessageType(player, "§cLe destinataire est en Jump, impossible de vous tp", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if(JumpManager.isJumping(player)) {
            MessageManager.sendMessageType(player, "§cVous êtes en Jump, impossible de vous tp", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        TPAQueue.INSTANCE.addRequest(player, target);
        MessageManager.sendMessageType(player, "§aDemande de téléportation envoyée à §e" + target.getName(), Prefix.TPA, MessageType.SUCCESS, true);

        final Component message = Component.text("§eTapez /tpaccept pour accepter.")
                .clickEvent(ClickEvent.runCommand("/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")));

        MessageManager.sendMessageType(target, "§a" + player.getName() + " vous a envoyé une demande de téléportation.", Prefix.TPA, MessageType.SUCCESS, true);
        plugin.getAdventure().player(target).sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                TPAQueue.INSTANCE.expireRequest(player, target);
            }
        }.runTaskLater(plugin, 2400);
    }
}
