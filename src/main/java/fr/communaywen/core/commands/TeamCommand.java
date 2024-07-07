package fr.communaywen.core.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.teams.menu.TeamListMenu;
import fr.communaywen.core.teams.menu.TeamMenu;
import fr.communaywen.core.teams.utils.MethodState;
import fr.communaywen.core.teams.utils.TeamUtils;
import fr.communaywen.core.utils.CommandUtils;
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

@Command({"team", "ekip", "gang", "clan", "faction", "guild", "equipe", "tribu"})
@Description("Gestion des teams")
@CommandPermission("ayw.command.teams")
public class TeamCommand {

    TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();

    @DefaultFor("~")
    public void sendHelp(Player player, ExecutableCommand command, CommandHelp<Component> help, @Default("1") @Range(min = 1) int page) {
        if(teamManager.isInTeam(player.getUniqueId())) {
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
        if (teamManager.isInTeam(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        TeamMenu teamMenu = new TeamMenu(player, team, false);
        teamMenu.open();
    }

    @Subcommand("create")
    @Description("Créer une team")
    public void createTeam(Player player, @Named("nom") String teamName) {
        TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();
        if (teamManager.isInTeam(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous êtes déjà dans une team !", true);
            return;
        }
        if (teamName.length() > 16) {
            CommandUtils.sendMessage(player, "Le nom de la team ne doit pas dépasser 16 caractères !", true);
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
        Menu menu = new TeamListMenu(player, AywenCraftPlugin.getInstance().getTeamManager());
        menu.open();
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
        if (team.isIn(player.getUniqueId())) {
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
        if (team.isIn(target.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        if (!team.isOwner(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas le propriétaire de la team !", true);
            return;
        }
        UUID targetUUID = target.getUniqueId();
        MethodState state = team.removePlayer(targetUUID);
        if (state == MethodState.VALID || state == MethodState.WARNING) CommandUtils.sendMessage(player, "Le joueur " + target.getName() + " a été kické de la team !", false);
        if (state == MethodState.INVALID) {
            CommandUtils.sendMessage(player, ChatColor.DARK_RED + "Impossible de kick, la team serait supprimée et il reste des items dans l'inventaire !", true);
            return;
        }
        if (state == MethodState.WARNING) CommandUtils.sendMessage(player, ChatColor.DARK_RED + "La team a été supprimée !", false);
        if (state == MethodState.VALID) {
            CommandUtils.sendMessage(target, ChatColor.DARK_RED + "Vous avez été kické de la team !", false);
        }
    }

    @Subcommand("leave")
    @Description("Quitter la team")
    public void leaveTeam(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        TeamUtils.quit(team, player);
    }

    @Subcommand("inventory")
    @Description("Inventaire de la team")
    public void teamInventory(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }
        team.openInventory(player);
    }

}