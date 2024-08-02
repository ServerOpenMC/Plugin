package fr.communaywen.core.staff.report;

import fr.communaywen.core.utils.database.DatabaseConnector;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.UUID;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Feature("Report")
@Credit("rafael_trx")

public class ReportManager extends DatabaseConnector {

    public boolean addReport(Player player, Player target, String reason, Timestamp timestamp) {

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO reports (sender, reported, reason, timestamp) VALUES (?, ?, ?, ?);");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, target.getUniqueId().toString());
            statement.setString(3, reason);
            statement.setTimestamp(4, timestamp);
            statement.executeUpdate();

            return true;

        } catch (SQLException ignored) {}
        return false;

    }

    public void seeHistory(Player player) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE sender = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();
            StringBuilder historyMessage = new StringBuilder();

            // Vérifier si des rapports ont été trouvés
            if (!rs.isBeforeFirst()) {
                player.sendMessage(" \n Vous n'avez effectué aucun signalement");
                return;
            }
            player.sendMessage("\nVos signalements : \n  \n");
            while (rs.next()) {
                // Récupérer les données de chaque colonne
                String reported = rs.getString("reported");
                String reason = rs.getString("reason");
                String timecode = rs.getString("timecode");

                OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(reported));
                String reportedName = reportedPlayer.getName();


                // Construire le message à envoyer au joueur
                historyMessage.append("Vous avez signalé ").append(ChatColor.GREEN).append(reportedName).append(ChatColor.WHITE).append("\n")
                        .append("Motif : ").append(reason).append("\n")
                        .append("Date : ").append(timecode).append("\n")
                        .append("--------\n");
            }

            // Envoyer tous les rapports au joueur
            player.sendMessage(historyMessage.toString());

        } catch (SQLException ignored) {}


    }

    public void seeReports(Player player, Player target) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE reported = ?");
            statement.setString(1, target.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();
            StringBuilder reportMessage = new StringBuilder();

            // Vérifier si des rapports ont été trouvés
            if (!rs.isBeforeFirst()) {
                player.sendMessage(" \n Aucun signalement pour " + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " !");
                return;
            }
            player.sendMessage("\n Signalements de " + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " : \n  \n");
            while (rs.next()) {
                // Récupérer les données de chaque colonne
                String sender = rs.getString("sender");
                String reported = rs.getString("reported");
                String reason = rs.getString("reason");
                String timecode = rs.getString("timecode");

                OfflinePlayer senderPlayer = Bukkit.getOfflinePlayer(UUID.fromString(sender));
                String senderName = senderPlayer.getName();


                // Construire le message à envoyer au joueur
                reportMessage.append("Signalé par : ").append(senderName).append("\n")
                        .append("Motif : ").append(reason).append("\n")
                        .append("Date : ").append(timecode).append("\n")
                        .append("--------\n");
            }

            // Envoyer tous les rapports au joueur
            player.sendMessage(reportMessage.toString());

        } catch (SQLException ignored) {}


    }

    public void topReports(Player player) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT reported, COUNT(*) AS num_reports FROM reports GROUP BY reported ORDER BY num_reports DESC LIMIT 5;");

            ResultSet rs = statement.executeQuery();
            StringBuilder topreportMessage = new StringBuilder();

            // Vérifier si des rapports ont été trouvés
            if (!rs.isBeforeFirst()) {
                player.sendMessage(" \n Aucun joueur n'a de signalement ! ");
                return;
            }
            player.sendMessage("\n Top des signalements : \n \n");
            while (rs.next()) {
                // Récupérer les données de chaque colonne
                String reported = rs.getString("reported");
                String count = rs.getString("num_reports");

                OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(reported));
                String reportedName = reportedPlayer.getName();


                // Construire le message à envoyer au joueur
                topreportMessage.append("- ").append(ChatColor.GREEN).append(reportedName).append(ChatColor.WHITE).append(" - ")
                        .append(count).append(" signalement(s)");

            }

            // Envoyer tous les rapports au joueur
            player.sendMessage(topreportMessage.toString());
            player.sendMessage("\n -------- \n");

        } catch (SQLException ignored) {}


    }

    public boolean clearReports(Player target) {

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM reports WHERE reported = ?;");
            statement.setString(1, target.getUniqueId().toString());

            statement.executeUpdate();

            return true;

        } catch (SQLException ignored) {}
        return false;


    }

    public boolean checkReportability(Player target) {
        try {

            Timestamp last24Hours = Timestamp.from(Instant.now().minus(24, ChronoUnit.HOURS));

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM reports WHERE reported = ? AND timestamp > ?");
            statement.setString(1, target.getUniqueId().toString());
            statement.setTimestamp(2, last24Hours);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("COUNT(*)");
                return count == 0;
            }
            else {
                return false;
            }

        } catch (SQLException ignored) {return false;}

    }

}

