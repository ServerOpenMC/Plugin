package fr.communaywen.core.staff.freeze;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class FreezeCommand implements CommandExecutor {

    private final AywenCraftPlugin plugin;

    public FreezeCommand(AywenCraftPlugin plugin) {

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
            player.sendMessage("§cUsage: /freeze [joueur]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cDésolé, le joueur n'est pas en ligne.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cTu ne peut pas te freeze toi même...");
            return true;
        }

        if (plugin.getFrozenPlayers().contains(target.getUniqueId())) {
            player.sendMessage("§cLe joueur est déjà freeze.");
            return true;
        }

        plugin.getFrozenPlayers().add(target.getUniqueId());
        target.sendTitle(ChatColor.RED + "Vous êtes freeze !", ChatColor.YELLOW + "Regardez votre chat", 10, 1000, 20);
        target.sendMessage(ChatColor.RED + "Vous êtes Freeze, si vous déconnectez, vous serez banni !");
        player.sendMessage(ChatColor.GREEN + target.getName() + " a bien été freeze.");
        return true;
    }
}