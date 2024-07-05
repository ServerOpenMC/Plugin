package fr.communaywen.core.commands;

import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Bukkit;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Balance: " + economyManager.getBalance(player));
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("transfer")) {
            Player target = Bukkit.getPlayer(args[1]);
            double amount;

            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid amount.");
                return true;
            }

            if (target != null && economyManager.transferBalance(player, target, amount)) {
                player.sendMessage("Transferred " + amount + " to " + target.getName());
                target.sendMessage("Received " + amount + " from " + player.getName());
            } else {
                player.sendMessage("Transfer failed.");
            }

            return true;
        }

        player.sendMessage("Usage: /money [transfer <player> <amount>]");
        return true;
    }
}
