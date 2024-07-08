package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
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

    public MoneyCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @DefaultFor("~")
    public void balance(Player player) {
        player.sendMessage("Balance: " + economyManager.getBalance(player));
    }

    @Subcommand("help")
    @Description("Afficher l'aide")
    public void sendHelp(BukkitCommandActor sender, CommandHelp<Component> help, ExecutableCommand thisHelpCommand, @Default("1") @Range(min = 1) int page) {
        Audience audience = AywenCraftPlugin.getInstance().getAdventure().sender(sender.getSender());
        AywenCraftPlugin.getInstance().getInteractiveHelpMenu().sendInteractiveMenu(audience, help, page, thisHelpCommand, "§b§lTEAM");
    }

    @Subcommand("transfer")
    @Description("Transférer de l'argent à un joueur")
    public void transfer(Player player, @Named("joueur") Player target, @Named("montant") int amount) {
        if (economyManager.transferBalance(player, target, amount)) {
            player.sendMessage("Transféré " + amount + " à " + target.getName());
            target.sendMessage("Reçu " + amount + " de " + player.getName());
        } else {
            player.sendMessage("Transfert échoué.");
        }
    }

    @Subcommand("add")
    @Description("Ajoute de l'argent à un joueur")
    @CommandPermission("openmc.money.add")
    public void add(Player player, @Named("joueur") Player target, @Named("montant") int amount) {
        economyManager.addBalance(target, amount);
        player.sendMessage("§aVous venez d'ajouter §f" + amount + " §aà " + target.getName());
        target.sendMessage("§aVous venez de recevoir §f" + amount);
    }

    @Subcommand("remove")
    @Description("Enleve de l'argent à un joueur")
    @CommandPermission("openmc.money.remove")
    public void remove(Player player, @Named("joueur") Player target, @Named("montant") int amount) {
        economyManager.withdrawBalance(target, amount);
        player.sendMessage("§aVous venez d'enlever §f" + amount + " §aà " + target.getName());
        target.sendMessage("§aVous venez de perdre §f" + amount);
    }




}
