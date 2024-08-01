package fr.communaywen.core.tpa;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TpdenyCommand {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Command("tpdeny")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player) {
        Player requester = tpQueue.getRequester(player);
        if (requester == null) {
            player.sendMessage("§cVous n'avez pas de demande de téléportation.");
            return;
        }

        tpQueue.removeRequest(player);
        requester.sendMessage("§c" + player.getName() + " a refusé votre demande de téléportation.");
        player.sendMessage("§cVous avez refusé la demande de téléportation de §f" + requester.getName() + "§c.");
    }
}

