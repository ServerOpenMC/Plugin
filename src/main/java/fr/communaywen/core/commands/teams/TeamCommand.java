package fr.communaywen.core.commands.teams;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.ClaimListener;
import fr.communaywen.core.claim.ClaimMenu;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.teams.EconomieTeam;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.teams.menu.TeamListMenu;
import fr.communaywen.core.teams.menu.TeamMenu;
import fr.communaywen.core.teams.utils.MethodState;
import fr.communaywen.core.teams.utils.TeamUtils;
import fr.communaywen.core.utils.CommandUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.CommandHelp;

import java.util.UUID;

@Command({"team"})
@Description("Gestion des teams")
@CommandPermission("ayw.command.teams")
public class TeamCommand {

    TeamManager teamManager = AywenCraftPlugin.getInstance().getManagers().getTeamManager();

    @DefaultFor("~")
    public void sendHelp(Player player, ExecutableCommand command, CommandHelp<Component> help, @Default("1") @Range(min = 1) int page) {
        if (teamManager.isInTeam(player.getUniqueId())) {
            Team team = teamManager.getTeamByPlayer(player.getUniqueId());
            TeamMenu teamMenu = new TeamMenu(player, team, false);
            teamMenu.open();
        } else {
            Audience audience = AywenCraftPlugin.getInstance().getAdventure().sender(player);
            AywenCraftPlugin.getInstance().getInteractiveHelpMenu().sendInteractiveMenu(audience, help, 1, command, "§b§lTEAM");
        }
    }

    @Subcommand("help")
    @Description("Afficher l'aide")
    public void sendHelp(BukkitCommandActor sender, CommandHelp<Component> help, ExecutableCommand thisHelpCommand, @Default("1") @Range(min = 1) int page) {
        Audience audience = AywenCraftPlugin.getInstance().getAdventure().sender(sender.getSender());
        AywenCraftPlugin.getInstance().getInteractiveHelpMenu().sendInteractiveMenu(audience, help, page, thisHelpCommand, "§b§lTEAM");
    }

    @Subcommand("menu")
    @Description("Menu de la team")
    public void teamMenu(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (!teamManager.isInTeam(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        TeamMenu teamMenu = new TeamMenu(player, team, false);
        teamMenu.open();
    }

    @Subcommand("create")
    @Description("Créer une team")
    public void createTeam(Player player, @Named("nom") String teamName) {
        TeamManager teamManager = AywenCraftPlugin.getInstance().getManagers().getTeamManager();
        if (teamManager.isInTeam(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous êtes déjà dans une team !", true);
            return;
        }
        if (teamName.length() > 16) {
            CommandUtils.sendMessage(player, "Le nom de la team ne doit pas dépasser 16 caractères !", true);
            return;
        }
        if (teamManager.teamExists(teamName)) {
            CommandUtils.sendMessage(player, "Une team avec ce nom existe déjà !", true);
            return;
        }
        Team createdTeam = teamManager.createTeam(player.getUniqueId(), teamName);
        boolean couldAdd = createdTeam.addPlayer(player.getUniqueId());
        if (!couldAdd) {
            CommandUtils.sendMessage(player, "La team est déjà au complet !", true);
            return;
        }
        CommandUtils.sendMessage(player, ChatColor.GREEN + "Vous avez créé la team " + createdTeam.getName() + " !", false);
    }

    @Subcommand("list")
    @Description("Liste des teams")
    public void listTeams(Player player) {
        Menu menu = new TeamListMenu(player, AywenCraftPlugin.getInstance().getManagers().getTeamManager());
        menu.open();
    }

    @Subcommand("chat")
    @Description("Envoyer un message à toute la team")
    public void chatTeam(Player player, @Named("message") String msg){
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());

        if(team == null) return;

        team.sendMessage(player, msg);
    }

    @Subcommand("invite")
    @Description("Inviter un joueur dans la team")
    public void invitePlayer(Player player, @Named("joueur") Player target) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team.isIn(target.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        if (!team.isOwner(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas le propriétaire de la team !", true);
            return;
        }
        if (teamManager.isInTeam(target.getUniqueId())) {
            CommandUtils.sendMessage(player, "Le joueur " + target.getName() + " est déjà dans une team !", true);
            return;
        }
        boolean couldInvite = teamManager.invite(target.getUniqueId(), team);
        if (!couldInvite) {
            CommandUtils.sendMessage(player, "La team est déjà au complet !", true);
            return;
        }

        //TODO: make component with kiory for clickable buttons
        CommandUtils.sendMessage(player, "Vous avez invité " + target.getName() + " dans la team !", false);
        CommandUtils.sendMessage(target, "Vous avez été invité dans la team " + team.getName() + " !", false);
        CommandUtils.sendMessage(target, "Pour accepter, faites " + ChatColor.GREEN + "/team accept", false);
    }

    @Subcommand("accept")
    @Description("Accepter une invitation")
    public void acceptInvite(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team != null && team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous êtes déjà dans une team !", true);
            return;
        }
        team = teamManager.acceptInvite(player.getUniqueId());
        if (team != null) {
            CommandUtils.sendMessage(player, ChatColor.GREEN + "Vous avez bien rejoint la team " + team.getName() + " !", false);
            for (UUID teamPlayer : team.getPlayers()) {
                Player teamPlayerOnline = Bukkit.getPlayer(teamPlayer);
                if (teamPlayerOnline != null) {
                    CommandUtils.sendMessage(teamPlayerOnline, player.getName() + " a rejoint la team !", false);
                }
            }
        } else {
            CommandUtils.sendMessage(player, "Vous n'avez pas d'invitation en attente !", true);
        }
    }

    @Subcommand("kick")
    @Description("Kick un joueur de la team")
    public void kickPlayer(Player player, @Named("joueur") Player target) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(target.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        if (!team.isOwner(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas le propriétaire de la team !", true);
            return;
        }
        UUID targetUUID = target.getUniqueId();
        MethodState state = team.removePlayer(targetUUID);
        if (state == MethodState.VALID || state == MethodState.WARNING)
            CommandUtils.sendMessage(player, "Le joueur " + target.getName() + " a été kické de la team !", false);
        if (state == MethodState.INVALID) {
            CommandUtils.sendMessage(player, ChatColor.DARK_RED + "Impossible de kick, la team serait supprimée et il reste des items dans l'inventaire !", true);
            return;
        }
        if (state == MethodState.WARNING)
            CommandUtils.sendMessage(player, ChatColor.DARK_RED + "La team a été supprimée !", false);
        if (state == MethodState.VALID) {
            CommandUtils.sendMessage(target, ChatColor.DARK_RED + "Vous avez été kické de la team !", false);
        }
    }

    @Subcommand("leave")
    @Description("Quitter la team")
    public void leaveTeam(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        TeamUtils.quit(team, player);
    }

    @Subcommand("inventory")
    @Description("Inventaire de la team")
    public void teamInventory(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (!team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        team.openInventory(player);
    }

//    @Subcommand("claim")
//    @Description("Gestion des claims")
//    @AutoComplete("@claimsTeams *")
//    public void claimMenu(Player player, @Default("~") @Named("claim") String claim) {
//        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
//        if (team == null || !team.isIn(player.getUniqueId())) {
//            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
//            return;
//        }
//
//        if(claim.equalsIgnoreCase("list")) {
//
//        } else if(claim.equalsIgnoreCase("bypass")) {
//
//        } else if(claim.equalsIgnoreCase("~")) {
//
//        }
//    }

    @Subcommand("money add")
    @Description("Transfère de l'argent de ton compte à la team.")
    public void transferMoney(Player player, @Named("montant") int amount) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        EconomyManager economyManager = AywenCraftPlugin.getInstance().getManagers().getEconomyManager();

        if(amount > 0 && economyManager.getBalance(player) >= amount) {
            AywenCraftPlugin.getInstance().getManagers().getEconomyManager().withdrawBalance(player, amount);
            EconomieTeam.addBalance(team.getName(), amount);
            player.sendMessage("§aVous venez de transférer §e" + amount + "$ §adans la banque de votre team.");
        } else {
            player.sendMessage("§cVous n'avez pas assez d'argent ou vous avez entré un montant invalide.");
        }
    }

    @Subcommand("money remove")
    @Description("Retire de l'argent de la banque de la team vers ton compte.")
    public void removeMoney(Player player, @Named("montant") int amount) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        double balances = EconomieTeam.getTeamBalances(team.getName());
        if(balances >= amount && amount > 0) {
            AywenCraftPlugin.getInstance().getManagers().getEconomyManager().addBalance(player, amount);
            EconomieTeam.removeBalance(team.getName(), amount);
            player.sendMessage("§aVous venez de prendre §e" + amount + "$ §ade la banque de votre team.");
        } else {
            player.sendMessage("§cVotre team n'a pas assez d'argent dans la banque, ou vous avez entré un montant invalide.");
        }
    }

    @Subcommand("money get")
    @Description("Voir l'argent de la team")
    public void getMoney(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        double balances = EconomieTeam.getTeamBalances(team.getName());
        player.sendMessage("§aIl y a §e" + balances + "$ §adans la banque de votre team.");
    }

}
