package fr.communaywen.core.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTPA implements CommandExecutor {
    // This method is called, when somebody uses our command

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/tpa <joueur>");
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver == null) {
                player.sendMessage("Impossible de trouver le joeur \""+args[0]+"\"");
                return false;
            }
            tpQueue.TPA_REQUESTS.put(receiver, new TPARequest(player));
            player.sendMessage("Vous avez envoyé une demande de tpa à " + receiver.getName());
            receiver.sendMessage(player.getName() + " vous a envoyé un demande de téléportation faites /tpaccept pour l'accepter");
            return true;
        }

        return false;
    }
}
