package fr.communaywen.core.tpa;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
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
            MessageManager.sendMessageType(player, "§cVous n'avez pas de demande de téléportation.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        tpQueue.removeRequest(player);
        MessageManager.sendMessageType(requester, "§c" + player.getName() + " a refusé votre demande de téléportation.", Prefix.TPA, MessageType.INFO, true);
        MessageManager.sendMessageType(player, "§cVous avez refusé la demande de téléportation de §f" + requester.getName() + "§c.", Prefix.TPA, MessageType.SUCCESS, true);
    }
}

