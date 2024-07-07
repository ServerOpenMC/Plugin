package fr.communaywen.core.commands;

import fr.communaywen.core.economy.EconomyManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MoneyCommand implements CommandExecutor, TabCompleter {

    private final EconomyManager economyManager;

    public MoneyCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            double balance = economyManager.getBalance(player);
            player.sendMessage(ChatColor.GOLD + "------------------------------");
            player.sendMessage(ChatColor.GREEN + "Votre solde actuel est de : " + ChatColor.YELLOW + balance + ChatColor.GREEN + " monnaie.");
            player.sendMessage(ChatColor.GOLD + "------------------------------");
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("transfer")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Le joueur spécifié est introuvable.");
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Montant invalide.");
                return true;
            }

            if (amount <= 0) {
                player.sendMessage(ChatColor.RED + "Le montant doit être positif.");
                return true;
            }

            if (economyManager.getBalance(player) < amount) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour effectuer ce transfert.");
                return true;
            }

            economyManager.withdraw(player, amount);
            economyManager.deposit(target, amount);

            player.sendMessage(ChatColor.GREEN + "Vous avez transféré " + ChatColor.YELLOW + amount + ChatColor.GREEN + " monnaie à " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");
            target.sendMessage(ChatColor.GREEN + "Vous avez reçu " + ChatColor.YELLOW + amount + ChatColor.GREEN + " monnaie de " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ".");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Syntaxe incorrecte. Utilisez /money ou /money transfer <joueur> <montant>.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("transfer");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("transfer")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
