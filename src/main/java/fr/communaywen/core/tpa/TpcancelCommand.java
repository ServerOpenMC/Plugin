package fr.communaywen.core.tpa;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
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
            MessageManager.sendMessageType(player, "§cVous n'avez pas de demande de téléportation.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        tpQueue.removeRequest(player);
        MessageManager.sendMessageType(player, "§cVous avez annulé votre demande de téléportation à §f" + target.getName() + "§c.", Prefix.TPA, MessageType.SUCCESS, true);
        MessageManager.sendMessageType(target, "§c" + player.getName() + " a annulé sa demande de téléportation.", Prefix.TPA, MessageType.INFO, true);
    }
}

