package fr.communaywen.core.staff.freeze;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.FreezeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class FreezeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("openmc.staff.freeze")) {
                if (args.length == 1) {
                    Player target = player.getServer().getPlayer(args[0]);
                    
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Ce joueur n'existe pas ou n'est pas connecté");
                        return false;
                    }
                    
                    FreezeUtils.switch_freeze(player, target);
                } else {
                    player.sendMessage(ChatColor.RED + "La commande est " + ChatColor.BLUE + "/freeze <joueur>");
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Hé, tu ne peut pas faire ça !");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "La commande ne peut seulement être exécutée par un joueur");
            return false;
        }
        return true;
    }
}