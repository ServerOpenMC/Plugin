package fr.communaywen.core.commands.corporation;

import fr.communaywen.core.corporation.guilds.Guild;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.guilds.menu.GuildBaltopMenu;
import fr.communaywen.core.corporation.guilds.menu.GuildMenu;
import fr.communaywen.core.corporation.guilds.GuildOwner;
import fr.communaywen.core.corporation.guilds.menu.GuildSearchMenu;
import fr.communaywen.core.corporation.shops.PlayerShopManager;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.MethodState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.UUID;

@Command("guild")
@Description("Gestion des guildes")
@CommandPermission("ayw.command.guild")
public class GuildCommand {

    private final GuildManager manager;
    private final TeamManager teamManager;
    private final EconomyManager economyManager;
    private final PlayerShopManager playerShopManager;

    public GuildCommand(GuildManager manager, TeamManager teamManager, EconomyManager economyManager, PlayerShopManager playerShopManager) {
        this.manager = manager;
        this.teamManager = teamManager;
        this.economyManager = economyManager;
        this.playerShopManager = playerShopManager;
    }

    @DefaultFor("~")
    public void onCommand(Player player) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage("Usage : /guild <baltop | balance | create | teamCreate | menu | search | apply | deny | accept | withdraw | deposit | leave | fire | owner | liquidate>");
            return;
        }
        GuildMenu menu = new GuildMenu(player, manager.getGuild(player.getUniqueId()), false);
        menu.open();
    }

    @Subcommand("apply")
    @Description("Postuler dans une guilde")
    public void apply(Player player, @Named("name") String name) {
        if (!manager.guildExists(name)) {
            player.sendMessage(ChatColor.RED + "La guilde n'existe pas !");
            return;
        }
        if (manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous êtes déjà dans une guilde !");
            return;
        }
        if (playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas postuler pour une guilde si vous possédez un shop !");
            return;
        }
        Guild guild = manager.getGuild(name);
        manager.applyToGuild(player.getUniqueId(), guild);
        player.sendMessage(ChatColor.GREEN + "Vous avez postulé pour la guilde " + name + " !");
        guild.broadCastOwner(ChatColor.GREEN + player.getName() + " a postulé pour rejoindre la guilde !");
    }

    @Subcommand("accept")
    @Description("Accepter une candidature")
    public void accept(Player player, @Named("target") Player target) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas un des propriétaires de la guilde !");
            return;
        }
        if (!manager.hasPendingApplicationFor(target.getUniqueId(), manager.getGuild(player.getUniqueId()))) {
            player.sendMessage(ChatColor.RED + "Le joueur n'a pas postulé pour votre guilde !");
            return;
        }
        Guild guild = manager.getGuild(player.getUniqueId());
        manager.acceptApplication(target.getUniqueId(), guild);
        player.sendMessage(ChatColor.GREEN + "Vous avez accepté la candidature de " + target.getName() + " !");
        target.sendMessage(ChatColor.GREEN + "Votre candidature pour la guilde <<" + guild.getName() + ">> a été acceptée !");
    }

    @Subcommand("deny")
    @Description("Refuser une candidature")
    public void deny(Player player, @Named("target") Player target) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas un des propriétaires de la guilde !");
            return;
        }
        if (!manager.hasPendingApplicationFor(target.getUniqueId(), manager.getGuild(player.getUniqueId()))) {
            player.sendMessage(ChatColor.RED + "Le joueur n'a pas postulé pour votre guilde !");
            return;
        }
        Guild guild = manager.getGuild(player.getUniqueId());
        manager.denyApplication(target.getUniqueId(), guild);
        player.sendMessage(ChatColor.GREEN + "Vous avez refusé la candidature de " + target.getName() + " !");
        target.sendMessage(ChatColor.RED + "Votre candidature pour la guilde <<" + guild.getName() + ">> a été refusée !");
    }

    @Subcommand("search")
    @Description("Rechercher une guilde")
    public void search(Player player) {
        GuildSearchMenu menu = new GuildSearchMenu(player, manager);
        menu.open();
    }

    @Subcommand("liquidate")
    @Description("Liquider une guilde")
    public void liquidate(Player player) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'etes dans aucune guilde !");
            return;
        }
        Guild guild = manager.getGuild(player.getUniqueId());
        if (!guild.isUniqueOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas le propriétaire haut-gradé de la guilde !");
            return;
        }
        if (!manager.liquidateGuild(guild)) {
            player.sendMessage(ChatColor.RED + "La guilde ne peut pas être liquidée car elle possède encore de l'argent (merci de withdraw)!");
            return;
        };
        player.sendMessage(ChatColor.GREEN + "La guilde a été liquidée avec succès !");
    }

    @Subcommand("leave")
    @Description("Quitter une guilde")
    public void leave(Player player) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        MethodState leaveResult = manager.leaveGuild(player.getUniqueId());
        if (leaveResult == MethodState.FAILURE) {
            player.sendMessage(ChatColor.RED + "Merci de transférer l'ownership avant de quitter la team !");
            return;
        }
        if (leaveResult == MethodState.WARNING) {
            player.sendMessage(ChatColor.RED + "Vous êtes le dernier a quitter et la guilde ne peut pas être liquidée car elle possède encore de l'argent (merci de withdraw)!");
            return;
        }
        if (leaveResult == MethodState.SPECIAL) {
            player.sendMessage(ChatColor.RED + "Le propriétaire de la team doit liquider ou quitter la guilde pour que vous puissiez ne plus en faire partie ! Ou vous pouvez quitter votre team pour quitter la guilde !");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Vous avez quitté la guilde !");
    }

    @Subcommand("fire")
    @Description("Renvoyer un membre de la guilde")
    public void fire(Player player, @Named("target") Player target) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas un des propriétaires de la guilde !");
            return;
        }
        if (!manager.isMerchantOfGuild(target.getUniqueId(), manager.getGuild(player.getUniqueId()))) {
            player.sendMessage(ChatColor.RED + "Ce marchand n'est pas trouvable dans la guilde !");
            return;
        }
        manager.getGuild(player.getUniqueId()).fireMerchant(target.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Vous avez renvoyé " + target.getName() + " de la guilde !");
    }

    @Subcommand("baltop")
    @Description("Afficher le top des guildes")
    public void baltop(Player player) {
        GuildBaltopMenu menu = new GuildBaltopMenu(player, manager);
        menu.open();
    }

    @Subcommand("balance")
    @Description("Afficher le solde de la guilde")
    public void balance(Player player) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Solde de la guilde : " + manager.getGuild(player.getUniqueId()).getBalance() + "€");
    }

    @Subcommand("create")
    @Description("Créer une guilde")
    public void createGuild(Player player, @Named("name") String name) {
        if (!check(player, name, false)) return;
        manager.createGuild(name, new GuildOwner(player.getUniqueId()), economyManager);
        player.sendMessage(ChatColor.GREEN + "La guilde " + name + " a été créée avec succès !");
    }

    @Subcommand("teamCreate")
    @Description("Créer une guilde de team")
    public void createTeamGuild(Player player, @Named("name") String name) {
        if (!check(player, name, true)) return;
        if (!teamManager.isInTeam(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une team !");
            return;
        }
        if (!teamManager.getTeamByPlayer(player.getUniqueId()).isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de guilde au nom de votre team sans en être l'owner");
            return;
        }
        manager.createGuild(name, new GuildOwner(teamManager.getTeamByPlayer(player.getUniqueId())), economyManager);
        player.sendMessage(ChatColor.GREEN + "La guilde " + name + " a été créée avec succès !");
    }

    @Subcommand("menu")
    @Description("Ouvrir le menu de guilde")
    public void openMenu(Player player) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        GuildMenu menu = new GuildMenu(player, manager.getGuild(player.getUniqueId()), false);
        menu.open();
    }

    @Subcommand("withdraw")
    @Description("Retirer de l'argent de la guilde")
    public void withdraw(Player player, @Named("amount") double amount) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous ne faites pas partie des propriétaires de la guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).withdraw(amount, player, "Retrait", economyManager)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent dans la guilde !");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Vous avez retiré " + amount + "€ de la guilde !");
    }

    @Subcommand("deposit")
    @Description("Déposer de l'argent dans la guilde")
    public void deposit(Player player, @Named("amount") double amount) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).deposit(amount, player, "Dépôt", economyManager)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent sur vous !");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Vous avez déposé " + amount + "€ dans la guilde !");
    }

    @Subcommand("owner")
    @Description("Transférer la propriété de la guilde")
    public void transferOwner(Player player, @Named("target") Player target) {
        if (!manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une guilde !");
            return;
        }
        if (!manager.getGuild(player.getUniqueId()).isUniqueOwner(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous ne faites pas partie des propriétaires haut gradés de la guilde !");
            return;
        }
        if (!manager.isMerchantOfGuild(target.getUniqueId(), manager.getGuild(player.getUniqueId()))) {
            player.sendMessage(ChatColor.RED + "Le joueur ne fait pas partie de la guilde !");
            return;
        }
        manager.getGuild(player.getUniqueId()).setOwner(target.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Vous avez transféré la propriété de la guilde à " + target.getName());
    }

    private boolean check(Player player, String name, boolean teamCreate) {
        if (manager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous êtes déjà dans une guilde !");
            return false;
        }
        if (name.length() < 3 || name.length() > 16){
            player.sendMessage(ChatColor.RED + "Le nom de la guilde doit faire entre 3 et 16 caractères !");
            return false;
        }
        if (manager.guildExists(name)) {
            player.sendMessage(ChatColor.RED + "Une guilde avec ce nom existe déjà !");
            return false;
        }
        if (playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de guilde si vous possédez un shop !");
            return false;
        }
        if (teamCreate) {
            if (teamManager.isInTeam(player.getUniqueId())) {
                Team team = teamManager.getTeamByPlayer(player.getUniqueId());
                for (UUID teamMember : team.getPlayers()) {
                    if (playerShopManager.hasShop(teamMember)) {
                        player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de guilde si un membre de votre team possède un shop !");
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
