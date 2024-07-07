package fr.communaywen.core.tpa;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TpcancelCommand {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Command("tpcancel")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player) {
        Player tpaplayer = tpQueue.TPA_REQUESTS2.get(player);
        if (tpaplayer == null) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
            return;
        }
        tpQueue.TPA_REQUESTS.remove(tpaplayer);
        tpQueue.TPA_REQUESTS2.remove(player);
        player.sendMessage("Vous avez annulé votre demande de tpa à "+tpaplayer.getName());
        tpaplayer.sendMessage(player.getName()+" a annulé sa demande de tpa");
    }
}
