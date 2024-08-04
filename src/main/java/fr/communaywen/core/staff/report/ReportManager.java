package fr.communaywen.core.staff.report;

import fr.communaywen.core.utils.database.DatabaseConnector;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Feature("Report")
@Credit("rafael_trx")

public class ReportManager extends DatabaseConnector {

    private final Map<UUID, List<Report>> reportCache = new HashMap<>();

    public class Report {
        private final UUID sender;
        private final UUID reported;
        private final String reason;
        private final Timestamp timestamp;

        public Report(UUID sender, UUID reported, String reason, Timestamp timestamp) {
            this.sender = sender;
            this.reported = reported;
            this.reason = reason;
            this.timestamp = timestamp;
        }

        // Getters
        public UUID getSender() { return sender; }
        public UUID getReported() { return reported; }
        public String getReason() { return reason; }
        public Timestamp getTimestamp() { return timestamp; }
    }

    public void loadReports() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID sender = UUID.fromString(rs.getString("sender"));
                UUID reported = UUID.fromString(rs.getString("reported"));
                String reason = rs.getString("reason");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                Report report = new Report(sender, reported, reason, timestamp);
                reportCache.computeIfAbsent(reported, k -> new ArrayList<>()).add(report);
            }
        } catch (SQLException ignored) {}
    }

    public void saveReports() {
        try {
            connection.setAutoCommit(false);
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM reports");
            deleteStatement.executeUpdate();

            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO reports (sender, reported, reason, timestamp) VALUES (?, ?, ?, ?)");
            for (List<Report> reports : reportCache.values()) {
                for (Report report : reports) {
                    insertStatement.setString(1, report.getSender().toString());
                    insertStatement.setString(2, report.getReported().toString());
                    insertStatement.setString(3, report.getReason());
                    insertStatement.setTimestamp(4, report.getTimestamp());
                    insertStatement.addBatch();
                }
            }
            insertStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {}
    }

    public boolean addReport(Player player, OfflinePlayer target, String reason, Timestamp timestamp) {
        UUID senderUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        Report report = new Report(senderUUID, targetUUID, reason, timestamp);

        reportCache.computeIfAbsent(targetUUID, k -> new ArrayList<>()).add(report);
        return true;
    }

    public void seeHistory(Player player) {
        UUID playerUUID = player.getUniqueId();
        List<Report> reports = reportCache.getOrDefault(playerUUID, new ArrayList<>());

        if (reports.isEmpty()) {
            player.sendMessage(" \nVous n'avez effectué aucun signalement");
            return;
        }

        StringBuilder historyMessage = new StringBuilder("\nVos signalements : \n  \n");
        for (Report report : reports) {
            OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(report.getReported());
            String reportedName = reportedPlayer.getName();

            historyMessage.append("Vous avez signalé ").append(ChatColor.GREEN).append(reportedName).append(ChatColor.WHITE).append("\n")
                    .append("Motif : ").append(report.getReason()).append("\n")
                    .append("Date : ").append(report.getTimestamp()).append("\n")
                    .append("--------\n");
        }
        player.sendMessage(historyMessage.toString());
    }

    public void seeReports(Player player, OfflinePlayer target) {
        UUID targetUUID = target.getUniqueId();
        List<Report> reports = reportCache.getOrDefault(targetUUID, new ArrayList<>());

        if (reports.isEmpty()) {
            player.sendMessage(" \nAucun signalement pour " + ChatColor.GREEN + target.getName() + ChatColor.WHITE + " !");
            return;
        }

        StringBuilder reportMessage = new StringBuilder("\nSignalements de " + ChatColor.GREEN + target.getName() + ChatColor.WHITE + " : \n  \n");
        for (Report report : reports) {
            OfflinePlayer senderPlayer = Bukkit.getOfflinePlayer(report.getSender());
            String senderName = senderPlayer.getName();

            reportMessage.append("Signalé par : ").append(senderName).append("\n")
                    .append("Motif : ").append(report.getReason()).append("\n")
                    .append("Date : ").append(report.getTimestamp()).append("\n")
                    .append("--------\n");
        }
        player.sendMessage(reportMessage.toString());
    }

    public void topReports(Player player) {
        Map<UUID, Integer> reportCount = new HashMap<>();
        for (List<Report> reports : reportCache.values()) {
            for (Report report : reports) {
                reportCount.put(report.getReported(), reportCount.getOrDefault(report.getReported(), 0) + 1);
            }
        }

        List<Map.Entry<UUID, Integer>> topReports = new ArrayList<>(reportCount.entrySet());
        topReports.sort((e1, e2) -> e2.getValue() - e1.getValue());

        if (topReports.isEmpty()) {
            player.sendMessage(" \nAucun joueur n'a de signalement ! ");
            return;
        }

        StringBuilder topReportMessage = new StringBuilder("\nTop des signalements : \n \n");
        for (int i = 0; i < Math.min(5, topReports.size()); i++) {
            Map.Entry<UUID, Integer> entry = topReports.get(i);
            OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(entry.getKey());
            String reportedName = reportedPlayer.getName();

            topReportMessage.append("- ").append(ChatColor.GREEN).append(reportedName).append(ChatColor.WHITE).append(" - ")
                    .append(entry.getValue()).append(" signalement(s)\n");
        }
        player.sendMessage(topReportMessage.toString());
        player.sendMessage("\n -------- \n");
    }

    public boolean clearReports(Player target) {
        UUID targetUUID = target.getUniqueId();
        if (reportCache.containsKey(targetUUID)) {
            reportCache.remove(targetUUID);
            return true;
        }
        return false;
    }

    public boolean checkReportability(OfflinePlayer target) {
        UUID targetUUID = target.getUniqueId();
        List<Report> reports = reportCache.getOrDefault(targetUUID, new ArrayList<>());

        Timestamp last24Hours = Timestamp.from(Instant.now().minus(24, ChronoUnit.HOURS));
        return reports.stream().noneMatch(report -> report.getTimestamp().after(last24Hours));
    }
}
