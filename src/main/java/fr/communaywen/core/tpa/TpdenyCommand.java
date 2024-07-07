package fr.communaywen.core.tpa;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TpdenyCommand {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Command("tpdeny")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player) {
        Player tpaplayer = tpQueue.TPA_REQUESTS.get(player);
        if (tpaplayer == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
            return;
        }
        tpQueue.TPA_REQUESTS.remove(player);
        tpQueue.TPA_REQUESTS2.remove(tpaplayer);
        tpaplayer.sendMessage(ChatColor.RED + player.getName() + " a refusé votre demande de téléportation");
        player.sendMessage("Vous avez refusé la demande de téléporation de "+tpaplayer.getName());
    }
}
