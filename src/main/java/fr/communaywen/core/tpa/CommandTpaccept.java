package fr.communaywen.core.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandTpaccept implements CommandExecutor {
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
            player.sendMessage(tpRequest.requeste.getName()+" va être téléporté à vous");
            tpRequest.requeste.sendTitle("Téléportation à "+player.getName()+" dans ...",null,0,20,10);
            tpRequest.requeste.teleport(player);
            tpQueue.TPA_REQUESTS.remove(player);
            return true;
        }
        return false;
    }
}
