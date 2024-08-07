package fr.communaywen.core.tpa;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TpcancelCommand {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Command("tpcancel")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player) {
        Player target = tpQueue.getRequester(player);
        if (target == null) {
            player.sendMessage("§cVous n'avez pas de demande de téléportation.");
            return;
        }

        tpQueue.removeRequest(player);
        player.sendMessage("§cVous avez annulé votre demande de téléportation à §f" + target.getName() + "§c.");
        target.sendMessage("§c" + player.getName() + " a annulé sa demande de téléportation.");
    }
}

