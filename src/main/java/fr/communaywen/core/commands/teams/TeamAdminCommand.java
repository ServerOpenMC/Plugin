package fr.communaywen.core.commands.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.UUID;

@Command("teamadmin")
@CommandPermission("ayw.mods.teams")
public class TeamAdminCommand {
    AywenCraftPlugin plugin;
    TeamManager teamManager;

    public TeamAdminCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        teamManager = plugin.getManagers().getTeamManager();
    }

    @Subcommand("byuser")
    @CommandPermission("ayw.mods.teams")
    public void byuser(CommandSender sender, OfflinePlayer player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null){
            sender.sendMessage("§cLe joueur n'est pas dans une équipe.");
            return;
        }

        sender.sendMessage("§6"+team.getName());
        for (UUID p: team.getPlayers()) {
            sender.sendMessage("- "+plugin.getServer().getOfflinePlayer(p).getName());
        }
    }

    @Subcommand("delete")
    @CommandPermission("ayw.mods.teams")
    public void deleteTeam(CommandSender sender, String teamName) {
        if (!teamManager.teamExists(teamName)) {
            sender.sendMessage("§cL'équipe '"+teamName+"' n'existe pas.");
            return;
        }

        Team team = teamManager.getTeamByName(teamName);
        teamManager.deleteTeam(team);
        sender.sendMessage("§aL'équipe '"+team.getName()+"' a bien été supprimée.");
    }

    @Subcommand("add")
    @CommandPermission("ayw.mods.teams")
    public void addPlayer(CommandSender sender, Player player, String teamName) {
        if (!teamManager.teamExists(teamName)) {
            sender.sendMessage("§cL'équipe '"+teamName+"' n'existe pas.");
            return;
        }

        if (teamManager.getTeamByPlayer(player.getUniqueId()) != null) {
            sender.sendMessage("§cLe joueur est déjà dans une équipe");
            return;
        }

        Team team = teamManager.getTeamByName(teamName);

        if (team == null) {
            sender.sendMessage("§cL'équipe '"+teamName+"' n'existe pas.");
            return;
        }

        team.addPlayer(player.getUniqueId());
        sender.sendMessage("§aLe joueur '"+player.getName()+"' a bien été ajouté à l'équipe '"+teamName+"'.");
    }

    @Subcommand("kick")
    @CommandPermission("ayw.mods.teams")
    public void removePlayer(CommandSender sender, Player player){
        if (!teamManager.isInTeam(player.getUniqueId())) {
            sender.sendMessage("§cCe joueur n'est pas dans une équipe.");
            return;
        }

        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        team.removePlayer(player.getUniqueId());
        sender.sendMessage("§aLe joueur '"+player.getName()+"' a bien été retiré de son équipe");
    }

    @Subcommand("clear")
    @CommandPermission("ayw.mods.teams")
    public void clear(CommandSender sender, String teamname) {
        if (!teamManager.teamExists(teamname)) {
            sender.sendMessage("§cL'équipe '"+teamname+"' n'existe pas.");
            return;
        }

        Team team = teamManager.getTeamByName(teamname);
        team.setInventory(new ItemStack[36]);
        sender.sendMessage("§aL'inventaire de l'équipe '"+teamname+"' a bien été vidée.");
    }

    @Subcommand("inventory")
    @Description("Open team inventory")
    public void openInventory(Player player, String teamName) {
        if (player.getWorld().getName().equals("dreamworld")) {
            player.sendMessage("§cVous n'avez pas accès à cette commande actuellement");
            return;
        }
        if (!teamManager.teamExists(teamName)) {
            player.sendMessage("§cL'équipe '"+teamName+"' n'existe pas.");
            return;
        }

        Team team = teamManager.getTeamByName(teamName);
        team.openInventory(player);
    }
}
