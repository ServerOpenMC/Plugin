package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("TPA")
@Credit({"ddemile", "misieur", "process"})
public class TPACommand {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    private AywenCraftPlugin plugin;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") Player target) {
        if (player == target) {
            player.sendMessage("Pourquoi pas ?");
            tpQueue.TPA_REQUESTS.put(target, player);
            tpQueue.TPA_REQUESTS2.put(player, target);
            tpQueue.TPA_REQUESTS_TIME.put(player, System.currentTimeMillis() / 1000);
            new BukkitRunnable() {
                @Override
                public void run() {
                    expire_tpa(player, target);
                }
            }.runTaskLater(AywenCraftPlugin.getInstance(), 2400);
            return;
        }

        if (tpQueue.TPA_REQUESTS2.containsKey(player)) {
            player.sendMessage("Vous avez déjà une demande de téléportation en attente...");
            return;
        }


        tpQueue.TPA_REQUESTS.put(target, player);
        tpQueue.TPA_REQUESTS2.put(player, target);
        tpQueue.TPA_REQUESTS_TIME.put(player, System.currentTimeMillis() / 1000);

        player.sendMessage("Vous avez envoyé une demande de tpa à " + target.getName());

        final TextComponent textComponent = Component.text(player.getName() + " vous a envoyé un demande de téléportation faites /tpaccept pour l'accepter")
                .color(TextColor.color(255, 255, 255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(textComponent);

        new BukkitRunnable() {
            @Override
            public void run() {
                expire_tpa(player, target);
            }
        }.runTaskLater(AywenCraftPlugin.getInstance(), 2400);
    }

    private void expire_tpa(Player player, Player target) {

        if (tpQueue.TPA_REQUESTS2.containsKey(player)) {
            if (tpQueue.TPA_REQUESTS_TIME.get(player) >= System.currentTimeMillis() / 1000 - (2400 / 20)) {
                player.sendMessage("Votre demande de téléportation a expirée...");
                tpQueue.TPA_REQUESTS.remove(target, player);
                tpQueue.TPA_REQUESTS2.remove(player, target);
            }
        }
    }

}

