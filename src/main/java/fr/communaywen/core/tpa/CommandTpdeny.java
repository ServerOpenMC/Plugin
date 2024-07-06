package fr.communaywen.core.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandTpdeny implements CommandExecutor {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            TPARequest tpRequest = tpQueue.TPA_REQUESTS.get(player);
            if (tpRequest == null) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
                return true;
            }
            tpRequest.requeste.sendMessage(ChatColor.RED + player.getName() + " a refusé votre demande de téléportation");
            tpQueue.TPA_REQUESTS.remove(player);
            return true;
        }

        return false;
    }
}
