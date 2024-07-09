package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TPACommand {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    private AywenCraftPlugin plugin;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") Player target) {
            if (player == target){
                player.sendMessage("Tu ne peux pas faire une demande de téléportation à toi-même !");
                return;
            }

        if (tpQueue.TPA_REQUESTS2.containsKey(player)) {
            player.sendMessage("Vous avez déjà une demande de téléportation en attente...");
            return;
        }


            tpQueue.TPA_REQUESTS.put(target, player);
            tpQueue.TPA_REQUESTS2.put(player, target);

            player.sendMessage("Vous avez envoyé une demande de tpa à " + target.getName());

            final TextComponent textComponent = Component.text(player.getName() + " vous a envoyé un demande de téléportation faites /tpaccept pour l'accepter")
                    .color(TextColor.color(255,255,255))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                    .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour accepter§7]")));

            plugin.getAdventure().player(target).sendMessage(textComponent);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {


                if (tpQueue.TPA_REQUESTS2.containsKey(player)) {
                    player.sendMessage("Votre demande de téléportation a expirée...");

                    tpQueue.TPA_REQUESTS.remove(target, player);
                    tpQueue.TPA_REQUESTS2.remove(player, target);

                }


            }
        };

        task.runTaskLater((Plugin) this, 2400);
    }

}

