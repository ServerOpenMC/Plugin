package fr.communaywen.core.commands;

import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private final EconomyManager economyManager;

    public MoneyCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                double balance = economyManager.getBalance(player);
                player.sendMessage(ChatColor.GOLD + "------------------------------");
                player.sendMessage(ChatColor.GREEN + "Votre solde actuel est de : " + ChatColor.YELLOW + balance + " " + ChatColor.GREEN + "monnaie.");
                player.sendMessage(ChatColor.GOLD + "------------------------------");
            } else {
                sender.sendMessage(ChatColor.RED + "Seuls les joueurs peuvent exécuter cette commande.");
            }
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("transfer")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Seuls les joueurs peuvent exécuter cette commande.");
                return true;
            }

            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Le joueur spécifié est introuvable.");
                return true;
            }

            try {
                double amount = Double.parseDouble(args[2]);
                if (amount <= 0) {
                    player.sendMessage(ChatColor.RED + "Le montant doit être positif.");
                    return true;
                }

                if (economyManager.getBalance(player) < amount) {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de monnaie.");
                    return true;
                }

                economyManager.withdraw(player, amount);
                economyManager.deposit(target, amount);
                player.sendMessage(ChatColor.GREEN + "Vous avez transféré " + ChatColor.YELLOW + amount + " " + ChatColor.GREEN + "monnaie à " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");
                target.sendMessage(ChatColor.GREEN + "Vous avez reçu " + ChatColor.YELLOW + amount + " " + ChatColor.GREEN + "monnaie de la part de " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ".");
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Le montant spécifié n'est pas valide.");
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /money [transfer <joueur> <montant>]");
        return true;
    }
}
