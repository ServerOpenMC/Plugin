package fr.communaywen.core.staff.freeze;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnfreezeCommand implements CommandExecutor {
    private final AywenCraftPlugin plugin;

    public UnfreezeCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cLa commande ne peut seulement être exécutée par un joueur");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("openmc.staff.freeze")) {
            player.sendMessage("§cHé, tu ne peut pas faire ça !");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /unfreeze [joueur]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cDésolé, le joueur n'est pas en ligne.");
            return true;
        }

        if (!plugin.getFrozenPlayers().contains(target.getUniqueId())) {
            player.sendMessage("§cLe joueur n'est pas freeze");
            return true;
        }

        plugin.getFrozenPlayers().remove(target.getUniqueId());
        target.resetTitle();
        player.sendMessage(ChatColor.GREEN + target.getName() + " a bien été unfreeze !");
        return true;
    }
}