package fr.communaywen.core.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.teams.menu.TeamListMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seul un joueur peut executer cette commande !");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage("Usage: /team [create/list/invite/accept/leave/kick]");
            return false;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                sender.sendMessage("Usage: /team create <nom de la team>");
                return false;
            }
            if (args[0].equalsIgnoreCase("list")) {
                Menu menu = new TeamListMenu(player, teamManager);
                menu.open();
            }
            if (args[0].equalsIgnoreCase("invite")) {
                sender.sendMessage("Usage: /team invite <joueur>");
                return false;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                if (teamManager.isInTeam(player) != null) {
                    sender.sendMessage("Vous êtes déjà dans une team !");
                    return false;
                }
                Team team = teamManager.acceptInvite(player);
                if (team != null) {
                    sender.sendMessage("Vous avez bien rejoint la team !");
                    for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                        if (offlinePlayer.isOnline()) {
                            offlinePlayer.getPlayer().sendMessage(player.getName() + " a rejoint la team !");
                        }
                    }
                } else {
                    sender.sendMessage("Vous n'avez pas d'invitation !");
                }
            }
            if (args[0].equalsIgnoreCase("leave")) {
                Team team = teamManager.isInTeam(player);
                if (team == null) {
                    sender.sendMessage("Vous n'êtes pas dans une team !");
                    return false;
                }
                boolean notDeleted = team.removePlayer(player);
                sender.sendMessage("Vous avez quitté la team !");
                if (!notDeleted) {
                    sender.sendMessage("La team a été supprimée !");
                }
                for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                    if (offlinePlayer.isOnline()) {
                        offlinePlayer.getPlayer().sendMessage(player.getName() + " a quitté la team !");
                    }
                }
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("create")) {
                StringBuilder teamName = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    teamName.append(args[i]).append(" ");
                }
                if (teamManager.isInTeam(player) != null) {
                    sender.sendMessage("Vous êtes déjà dans une team !");
                    return false;
                }
                if (teamName.length() > 16) {
                    sender.sendMessage("Le nom de la team ne doit pas dépasser 16 caractères !");
                    return false;
                }
                Team createdTeam = teamManager.createTeam(player, teamName.toString().trim());
                boolean couldAdd = createdTeam.addPlayer(player);
                if (!couldAdd) {
                    sender.sendMessage("La team est déjà au complet !");
                    return false;
                }
                sender.sendMessage("La team " + teamName.toString().trim() + " a bien été créée !");
            }
            if (args[0].equalsIgnoreCase("invite")) {
                Team team = teamManager.isInTeam(player);
                if (team == null) {
                    sender.sendMessage("Vous n'êtes pas dans une team !");
                    return false;
                }
                if (!team.isOwner(player)) {
                    sender.sendMessage("Vous n'êtes pas le propriétaire de la team !");
                    return false;
                }
                Player target = player.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("Le joueur " + args[1] + " n'est pas connecté !");
                    return false;
                }
                if (teamManager.isInTeam(target) != null) {
                    sender.sendMessage("Le joueur " + target.getName() + " est déjà dans une team !");
                    return false;
                }
                boolean couldInvite = teamManager.invite(target, team);
                if (!couldInvite) {
                    sender.sendMessage("La team est déjà au complet !");
                    return false;
                }
                sender.sendMessage("Vous avez invité " + target.getName() + " dans votre team !");
                target.sendMessage("Vous avez été invité dans la team " + team.getName() + " !");
                target.sendMessage("Faites /team accept pour rejoindre la team !");
            }
            if (args[0].equalsIgnoreCase("kick")) {
                Team team = teamManager.isInTeam(player);
                if (team == null) {
                    sender.sendMessage("Vous n'êtes pas dans une team !");
                    return false;
                }
                if (!team.isOwner(player)) {
                    sender.sendMessage("Vous n'êtes pas le propriétaire de la team !");
                    return false;
                }
                OfflinePlayer target = team.getPlayer(args[1]);
                if (target != null) {
                    boolean notDeleted = team.removePlayer(target);
                    sender.sendMessage("Vous avez kické " + target.getName() + " de la team !");
                    if (target.isOnline()) {
                        Objects.requireNonNull(target.getPlayer()).sendMessage("Vous avez été kické de la team !");
                    }
                    if (!notDeleted) {
                        sender.sendMessage("La team a été supprimée !");
                    }
                } else {
                    sender.sendMessage("Le joueur " + args[1] + " n'est pas dans votre team !");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("create", "list", "invite", "accept", "leave", "kick");
        }
        if (args.length == 2)
        {
            if (sender instanceof Player player) {
                if (args[0].equalsIgnoreCase("create")) {
                    return List.of(player.getName() + "'s team");
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();
                    Team team = teamManager.isInTeam(player);
                    if (team != null) {
                        List<String> players = new ArrayList<>();
                        for (OfflinePlayer offlinePlayer : player.getServer().getOnlinePlayers()) {
                            if (teamManager.isInTeam(offlinePlayer) == null) {
                                players.add(offlinePlayer.getName());
                            }
                        }
                        return players;
                    }

                }
                if (args[0].equalsIgnoreCase("kick")) {
                    Team team = AywenCraftPlugin.getInstance().getTeamManager().isInTeam(player);
                    if (team != null) {
                        return team.getPlayers().stream().map(OfflinePlayer::getName).toList();
                    }
                }
            }
        }
        return List.of();
    }
}
