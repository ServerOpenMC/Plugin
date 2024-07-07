package fr.communaywen.core.tpa;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpcancel implements CommandExecutor {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player tpaplayer = tpQueue.TPA_REQUESTS2.get(player);
            if (tpaplayer == null) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
                return true;
            }
            player.sendMessage(ChatColor.RED + player.getName() + " a refusé votre demande de téléportation");
            tpQueue.TPA_REQUESTS.remove(tpaplayer);
            tpQueue.TPA_REQUESTS2.remove(player);
            return true;
        }

        return false;
    }
}
