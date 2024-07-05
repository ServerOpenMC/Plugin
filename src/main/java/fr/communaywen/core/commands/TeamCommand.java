package fr.communaywen.core.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.teams.menu.TeamListMenu;
import fr.communaywen.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();
        if (!(sender instanceof Player player)) {
            return CommandUtils.sendMessage(sender, "Vous devez être un joueur pour exécuter cette commande !", true);
        }
        if (args.length == 0) {
            return CommandUtils.sendMessage(sender, ChatColor.WHITE + "Usage: /team <create|list|invite|accept|leave|kick>", false);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                return CommandUtils.sendMessage(sender, ChatColor.WHITE + "Usage: /team create <nom de la team>", false);
            }
            if (args[0].equalsIgnoreCase("list")) {
                Menu menu = new TeamListMenu(player, teamManager);
                menu.open();
            }
            if (args[0].equalsIgnoreCase("invite")) {
                return CommandUtils.sendMessage(sender, ChatColor.WHITE + "Usage: /team invite <joueur>", true);
            }
            if (args[0].equalsIgnoreCase("accept")) {
                if (teamManager.isInTeam(player.getUniqueId()) != null) {
                    return CommandUtils.sendMessage(sender, "Vous êtes déjà dans une team !", true);
                }
                Team team = teamManager.acceptInvite(player.getUniqueId());
                if (team != null) {
                    CommandUtils.sendMessage(sender, ChatColor.GREEN + "Vous avez bien rejoint la team " + team.getName() + " !", false);
                    for (UUID teamPlayer : team.getPlayers()) {
                        Player teamPlayerOnline = Bukkit.getPlayer(teamPlayer);
                        if (teamPlayerOnline != null) {
                            CommandUtils.sendMessage(teamPlayerOnline, player.getName() + " a rejoint la team !", false);
                        }
                    }
                } else {
                    return CommandUtils.sendMessage(sender, "Vous n'avez pas d'invitation en attente !", true);
                }
            }
            if (args[0].equalsIgnoreCase("kick")) {
                return CommandUtils.sendMessage(sender, ChatColor.WHITE + "Usage: /team kick <joueur>", true);
            }
            if (args[0].equalsIgnoreCase("leave")) {
                Team team = teamManager.isInTeam(player.getUniqueId());
                if (team == null) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas dans une team !", true);
                }
                boolean notDeleted = team.removePlayer(player.getUniqueId());
                CommandUtils.sendMessage(sender, "Vous avez quitté la team !", false);
                if (!notDeleted) {
                    CommandUtils.sendMessage(sender, ChatColor.DARK_RED + "La team a été supprimée !", false);
                }
                for (UUID teamPlayer : team.getPlayers()) {
                    Player teamPlayerOnline = Bukkit.getPlayer(teamPlayer);
                    if (teamPlayerOnline != null) {
                        CommandUtils.sendMessage(teamPlayerOnline, player.getName() + " a quitté la team !", false);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("inventory")) {
                Team team = teamManager.isInTeam(player.getUniqueId());
                if (team == null) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas dans une team !", true);
                }
                team.openInventory(player);
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("create")) {
                StringBuilder teamName = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    teamName.append(args[i]).append(" ");
                }
                if (teamManager.isInTeam(player.getUniqueId()) != null) {
                    return CommandUtils.sendMessage(sender, "Vous êtes déjà dans une team !", true);
                }
                if (teamName.length() > 16) {
                    return CommandUtils.sendMessage(sender, "Le nom de la team ne doit pas dépasser 16 caractères !", true);
                }
                Team createdTeam = teamManager.createTeam(player.getUniqueId(), teamName.toString().trim());
                boolean couldAdd = createdTeam.addPlayer(player.getUniqueId());
                if (!couldAdd) {
                    return CommandUtils.sendMessage(sender, "La team est déjà au complet !", true);
                }
                CommandUtils.sendMessage(sender, ChatColor.GREEN + "Vous avez créé la team " + createdTeam.getName() + " !", false);
            }
            if (args[0].equalsIgnoreCase("invite")) {
                Team team = teamManager.isInTeam(player.getUniqueId());
                if (team == null) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas dans une team !", true);
                }
                if (!team.isOwner(player.getUniqueId())) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas le propriétaire de la team !", true);
                }
                Player target = player.getServer().getPlayer(args[1]);
                if (target == null) {
                    return CommandUtils.sendMessage(sender, "Le joueur " + args[1] + " n'est pas en ligne !", true);
                }
                if (teamManager.isInTeam(target.getUniqueId()) != null) {
                    return CommandUtils.sendMessage(sender, "Le joueur " + target.getName() + " est déjà dans une team !", true);
                }
                boolean couldInvite = teamManager.invite(target.getUniqueId(), team);
                if (!couldInvite) {
                    return CommandUtils.sendMessage(sender, "La team est déjà au complet !", true);
                }
                CommandUtils.sendMessage(sender, "Vous avez invité " + target.getName() + " dans la team !", false);
                CommandUtils.sendMessage(target, "Vous avez été invité dans la team " + team.getName() + " !", false);
                CommandUtils.sendMessage(target, "Pour accepter, faites " + ChatColor.GREEN + "/team accept", false);
            }
            if (args[0].equalsIgnoreCase("kick")) {
                Team team = teamManager.isInTeam(player.getUniqueId());
                if (team == null) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas dans une team !", true);
                }
                if (!team.isOwner(player.getUniqueId())) {
                    return CommandUtils.sendMessage(sender, "Vous n'êtes pas le propriétaire de la team !", true);
                }
                UUID target = team.getPlayerByUsername(args[1]);
                if (target != null) {
                    boolean notDeleted = team.removePlayer(target);
                    CommandUtils.sendMessage(sender, "Le joueur " + args[1] + " a été kické de la team !", false);
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                        CommandUtils.sendMessage(Objects.requireNonNull(targetPlayer), ChatColor.DARK_RED + "Vous avez été kické de la team !", false);
                    }
                    if (!notDeleted) {
                        CommandUtils.sendMessage(sender, ChatColor.DARK_RED + "La team a été supprimée !", false);
                    }
                } else {
                    return CommandUtils.sendMessage(sender, "Le joueur " + args[1] + " n'est pas dans la team !", true);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("create", "list", "invite", "accept", "leave", "kick", "inventory");
        }
        if (args.length == 2)
        {
            if (sender instanceof Player player) {
                if (args[0].equalsIgnoreCase("create")) {
                    return List.of(player.getName() + "'s team");
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    TeamManager teamManager = AywenCraftPlugin.getInstance().getTeamManager();
                    Team team = teamManager.isInTeam(player.getUniqueId());
                    if (team != null) {
                        List<String> players = new ArrayList<>();
                        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                            if (teamManager.isInTeam(onlinePlayer.getUniqueId()) == null) {
                                players.add(onlinePlayer.getName());
                            }
                        }
                        return players;
                    }

                }
                if (args[0].equalsIgnoreCase("kick")) {
                    Team team = AywenCraftPlugin.getInstance().getTeamManager().isInTeam(player.getUniqueId());
                    if (team != null) {
                        List<String> players = new ArrayList<>();
                        for (UUID uuid : team.getPlayers()) {
                            players.add(Bukkit.getOfflinePlayer(uuid).getName());
                        }
                        return players;
                    }
                }
            }
        }
        return List.of();
    }
}
