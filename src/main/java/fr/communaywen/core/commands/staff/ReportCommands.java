package fr.communaywen.core.commands.staff;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.staff.report.ReportManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import org.bukkit.ChatColor;
import java.sql.Timestamp;


@Feature("Report")
@Credit("rafael_trx")
@Command("report")
public class ReportCommands {
    @DefaultFor("~")
    @Description("Signale un joueur pour un motif spécifique")
    @CommandPermission("ayw.command.report")

    public boolean report(CommandSender sender, @Named("Joueur") Player target, @Named("Motif") String reason) {
        ReportManager reportManager = AywenCraftPlugin.getInstance().getManagers().getReportManager();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String senderName = sender.getName();
            String reportedPlayer = target.getName();
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            if (reportedPlayer.equals(senderName)) {
                player.sendMessage("Vous ne pouvez pas vous signaler vous-même");
                return false;
            }
            if(reportManager.checkReportability(target)) {
                if (reportManager.addReport(player, target, reason, currentTimestamp)) {
                    player.sendMessage("Vous avez signalé " + ChatColor.GREEN + reportedPlayer + ChatColor.WHITE + " pour : " + reason);
                    /* Insérer ce report dans la base de données */
                    return true;
                }
            }
            else{
                player.sendMessage("Votre dernier signalement de " + ChatColor.GREEN + reportedPlayer + ChatColor.WHITE + " est trop récent");
            }
        }
        else {
            return false;
        }
        return false;
    }

    @Subcommand("history")
    @Description("Permet de voir son historique de signalement")
    @CommandPermission("ayw.command.report")

    public boolean history(CommandSender sender) {
        ReportManager reportManager = AywenCraftPlugin.getInstance().getManagers().getReportManager();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            reportManager.seeHistory(player);
            return true;

        }
        else {
            return false;
        }
    }


    @Subcommand("see")
    @Description("Consulte les signalements d'un joueur")
    @CommandPermission("ayw.mods.report")

    public boolean see(CommandSender sender, @Named("Joueur") Player target) {
        ReportManager reportManager = AywenCraftPlugin.getInstance().getManagers().getReportManager();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            reportManager.seeReports(player, target);

            return true;
        }
        else {
            return false;
        }
    }


    @Subcommand("toplist")
    @Description("Affiche les 5 joueurs les plus signalés")
    @CommandPermission("ayw.mods.report")

    public boolean toplist(CommandSender sender) {
        ReportManager reportManager = AywenCraftPlugin.getInstance().getManagers().getReportManager();

            Player player = (Player) sender;

            reportManager.topReports(player);
            return true;


    }

    @Subcommand("clear")
    @Description("Consulte les signalements d'un joueur")
    @CommandPermission("ayw.mods.report")

    public boolean clear(CommandSender sender, @Named("Joueur") Player target) {
        ReportManager reportManager = AywenCraftPlugin.getInstance().getManagers().getReportManager();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(reportManager.clearReports(target)){
                player.sendMessage("Les signalements de " + player.getName() + " ont été supprimés");
                return true;
            }
            else {
                return false;
            }


        }
        else {
            return false;
        }
    }




}


