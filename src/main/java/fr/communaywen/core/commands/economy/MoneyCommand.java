package fr.communaywen.core.commands.economy;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.Managers;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.Transaction;
import fr.communaywen.core.utils.TransactionsMenu;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import fr.communaywen.core.utils.database.TransactionsManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.CommandHelp;

@Command("money")
@Description("Transférer de l'argent entre joueurs")
public class MoneyCommand {
    private final EconomyManager economyManager;
    AywenCraftPlugin plugin;
    TransactionsManager transactionsManager;

    public MoneyCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getManagers().getEconomyManager();
        this.transactionsManager = plugin.getManagers().getTransactionsManager();
    }

    @DefaultFor("~")
    public void balance(Player player, @Default("self") OfflinePlayer target) {
        if(target == player) {
            MessageManager.sendMessage(player, "§aVotre argent: §e" + economyManager.getFormattedBalance(target.getUniqueId()), Prefix.OPENMC, MessageType.INFO);
            return;
        }
        MessageManager.sendMessage(player, "§aArgent de §2" + target.getName() + "§a: §e" + economyManager.getFormattedBalance(target.getUniqueId()), Prefix.OPENMC, MessageType.INFO);
    }

    @Subcommand("help")
    @Description("Afficher l'aide")
    public void sendHelp(BukkitCommandActor sender, CommandHelp<Component> help, ExecutableCommand thisHelpCommand, @Default("1") @Range(min = 1) int page) {
        Audience audience = AywenCraftPlugin.getInstance().getAdventure().sender(sender.getSender());
        AywenCraftPlugin.getInstance().getInteractiveHelpMenu().sendInteractiveMenu(audience, help, page, thisHelpCommand, "§a§lMONEY");
    }

    @Subcommand("history")
    @Description("Affiche votre historique de transactions")
    public void history(CommandSender sender, @Optional Player target){
        if (!(sender instanceof Player player)) { return; }

        if (target == null) {
            target = player;
        } else {
            if (!sender.hasPermission("ayw.mods.history")){
                target = player;
            }
        }

        new TransactionsMenu(player, target.getUniqueId()).open();
    }


    @Subcommand("transfer")
    @Description("Transfère de l'argent d'un joueur à un autre.")
    public void transfer(Player player, @Named("joueur") Player target, @Named("montant") @Range(min = 1) int amount) {
        if(!player.equals(target)) {
            if (economyManager.transferBalance(player.getUniqueId(), target.getUniqueId(), amount)) {
                player.sendMessage("§aVous venez de transférer §e" + amount + "$ §aà §e" + target.getName());
                target.sendMessage("§aVous venez de recevoir §e" + amount + "$ §ade la part de §e" + player.getName());

                transactionsManager.addTransaction(new Transaction(
                        target.getUniqueId().toString(),
                        player.getUniqueId().toString(),
                        amount,
                        "Transfert"
                ));
            } else {
                player.sendMessage("§cVous n'avez pas assez d'argent.");
            }
        } else {
            player.sendMessage("§cVous ne pouvez pas transférer de l'argent à vous-même.");
        }
    }

    @Subcommand("add")
    @Description("Ajoute de l'argent à un joueur")
    @CommandPermission("openmc.money.add")
    public void add(Player player, @Named("joueur") Player target, @Named("montant") @Range(min = 1) int amount) {
        economyManager.addBalance(target.getUniqueId(), amount);
        player.sendMessage("§aVous venez d'ajouter §e" + amount + "$ §aà " + target.getName());
        target.sendMessage("§aVous venez de recevoir §e" + amount + "$");
        transactionsManager.addTransaction(new Transaction(
                player.getUniqueId().toString(),
                "CONSOLE",
                amount,
                "Give"
        ));
    }

    @Subcommand("remove")
    @Description("Enlève de l'argent à un joueur")
    @CommandPermission("openmc.money.remove")
    public void remove(Player player, @Named("joueur") Player target, @Named("montant") @Range(min = 1) int amount) {
        economyManager.withdrawBalance(target.getUniqueId(), amount);
        player.sendMessage("§aVous venez d'enlever §e" + amount + "$ §aà " + target.getName());
        target.sendMessage("§aVous venez de perdre §e" + amount + "$");
        transactionsManager.addTransaction(new Transaction(
                player.getUniqueId().toString(),
                "CONSOLE",
                amount,
                "Remove"
        ));
    }
}
