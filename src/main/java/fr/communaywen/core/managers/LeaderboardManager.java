package fr.communaywen.core.managers;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.commands.economy.BaltopCommand;
import fr.communaywen.core.teams.EconomieTeam;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.GitHubAPI;
import fr.communaywen.core.utils.database.DatabaseConnector;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

import static fr.communaywen.core.commands.economy.BaltopCommand.getColor;

public class LeaderboardManager extends DatabaseConnector {
    static FileConfiguration config;
    static AywenCraftPlugin plugins;
    private static final ArrayList<String> listLb = new ArrayList<>();

    public LeaderboardManager(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;

        listLb.add("playtime");
        listLb.add("baltop");
        listLb.add("teamtop");
        listLb.add("contribution");
        listLb.add("jump_record");
    }

    public static List<String> getLbList() {
        List<String> lbs = new ArrayList<>();
        for (String lbName : listLb) {
            lbs.add(lbName);
        }
        return lbs;
    }

    private static TextDisplay textDisplayBalTop;

    public static void createLeaderboardBalTop() {
        World world = Bukkit.getWorld((String) config.get("leaderboard.baltop.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.baltop.posX"), config.getDouble("leaderboard.baltop.posY"), config.getDouble("leaderboard.baltop.posZ"));

        textDisplayBalTop = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayBalTop.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayBalTop.setViewRange(100.0F);
        textDisplayBalTop.setDefaultBackground(false);
        textDisplayBalTop.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayBalTop.setCustomNameVisible(false);
        textDisplayBalTop.setCustomName("money");
    }

    public static void updateLeaderboardBalTop() {
        if (textDisplayBalTop == null) return;
        List<BaltopCommand.PlayerBalance> balances = BaltopCommand.getBalances();
        balances.sort((a, b) -> a.balance.intValue() - b.balance.intValue());

        balances = balances.reversed();

        if (balances.size() > 10) {
            balances = balances.subList(0, 10);
        }

        List<String> lines = new ArrayList<>();
        lines.add("§dLes §f10 §dJoueurs les plus riches sur le serveur");

        int index = 1;
        for (BaltopCommand.PlayerBalance playerBalance : balances) {
            String playerName = playerBalance.playerId.toString();
            if (Bukkit.getPlayer(playerBalance.playerId) != null) {
                playerName = Bukkit.getOfflinePlayer(playerBalance.playerId).getName();
            } else if (Bukkit.getOfflinePlayer(playerBalance.playerId) != null) {
                playerName = Bukkit.getOfflinePlayer(playerBalance.playerId).getName();
            }
            lines.add(MessageFormat.format("{0}# {1}: {2}", getColor(index) + index, ChatColor.GRAY + playerName, ChatColor.DARK_PURPLE + playerBalance.balance.toString()));

            index++;
        }

        String leaderboardText = String.join("\n", lines);
        textDisplayBalTop.setText(Component.text(leaderboardText).content());
    }

    public static void removeLeaderboardBalTop() {
        if (textDisplayBalTop != null && !textDisplayBalTop.isDead()) {
            textDisplayBalTop.remove();
            textDisplayBalTop = null;
        }
    }

    private static TextDisplay textDisplayTeamTop;

    public static void createLeaderboardTeamTop() {
        World world = Bukkit.getWorld((String) config.get("leaderboard.teamtop.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.teamtop.posX"), config.getDouble("leaderboard.teamtop.posY"), config.getDouble("leaderboard.teamtop.posZ"));

        textDisplayTeamTop = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayTeamTop.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayTeamTop.setViewRange(100.0F);
        textDisplayTeamTop.setDefaultBackground(false);
        textDisplayTeamTop.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayTeamTop.setCustomNameVisible(false);
        textDisplayTeamTop.setCustomName("teamtop");
    }

    public static void updateLeaderboardTeamTop() {
        if (textDisplayTeamTop == null) return;

        List<Team> teamBalances = TeamManager.getTeams();

        teamBalances.sort((a, b) -> Double.compare(b.getBalance(), a.getBalance()));

        if (teamBalances.size() > 10) {
            teamBalances = teamBalances.subList(0, 10);
        }

        List<String> lines = new ArrayList<>();
        lines.add("§dLes §f10 §dTeams les plus riches sur le serveur");

        int index = 1;
        for (Team team : teamBalances) {
            String teamName = team.getName();
            lines.add(MessageFormat.format("{0}# {1}: {2}", getColor(index) + index, ChatColor.GRAY + teamName, ChatColor.DARK_PURPLE + String.format("%.1f", EconomieTeam.getTeamBalances(team.getName()).doubleValue())));
            index++;
        }

        String leaderboardText = String.join("\n", lines);

        textDisplayTeamTop.setText(Component.text(leaderboardText).content());
    }

    public static void removeLeaderboardTeamTop() {
        if ((textDisplayTeamTop != null) && !textDisplayTeamTop.isDead()) {
            textDisplayTeamTop.remove();
            textDisplayTeamTop = null;
        }
    }

    private static TextDisplay textDisplayContribution;

    public static void createLeaderboardContribution() {
        World world = Bukkit.getWorld((String) config.get("leaderboard.contribution.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.contribution.posX"), config.getDouble("leaderboard.contribution.posY"), config.getDouble("leaderboard.contribution.posZ"));

        textDisplayContribution = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayContribution.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayContribution.setViewRange(100.0F);
        textDisplayContribution.setDefaultBackground(false);
        textDisplayContribution.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayContribution.setCustomNameVisible(false);
        textDisplayContribution.setCustomName("contribution");
    }

    public static void updateLeaderboardContribution() throws IOException {
        if (textDisplayContribution == null) return;

        List<String> lines = new ArrayList<>();
        lines.add("§dClassement des §f10 §dMeilleurs Contributeurs");
        GitHubAPI gitHubAPI = new GitHubAPI();
        String jsonResponse = gitHubAPI.getContributors();
        int index = 1;

        if (jsonResponse.startsWith("Error:")) {
            lines.add(ChatColor.RED + "Erreur :(");
        } else {
            JSONArray contributors = new JSONArray(jsonResponse);
            for (int i = 0; i < contributors.length() && i < 10; i++) {
                JSONObject contributor = contributors.getJSONObject(i);
                String login = contributor.getString("login");
                lines.add(MessageFormat.format("{0}# {1}", getColor(index) + index, ChatColor.GRAY + login));
                index++;
            }
        }

        String leaderboardText = String.join("\n", lines);

        textDisplayContribution.setText(Component.text(leaderboardText).content());
    }

    public static void removeLeaderboardContribution() {
        if ((textDisplayContribution != null) && !textDisplayContribution.isDead()) {
            textDisplayContribution.remove();
            textDisplayContribution = null;
        }
    }

    private static TextDisplay textDisplayPlayTime;

    public static void createLeaderboardPlayTime() {
        World world = Bukkit.getWorld((String) config.get("leaderboard.playtime.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.playtime.posX"), config.getDouble("leaderboard.playtime.posY"), config.getDouble("leaderboard.playtime.posZ"));

        textDisplayPlayTime = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayPlayTime.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayPlayTime.setViewRange(100.0F);
        textDisplayPlayTime.setDefaultBackground(false);
        textDisplayPlayTime.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayPlayTime.setCustomNameVisible(false);
        textDisplayPlayTime.setCustomName("playtime");
    }

    public static void updateLeaderboardPlayTime() {
        if (textDisplayPlayTime == null) return;

        List<Map.Entry<UUID, Long>> topPlayTime = getTopPlayTime(10);

        List<String> lines = new ArrayList<>();
        lines.add("§dLes §f10 §dMeilleurs Temps de Jeu des Joueurs");

        int index = 1;
        for (Map.Entry<UUID, Long> entry : topPlayTime) {
            UUID playerUUID = entry.getKey();
            long time = entry.getValue();

            String playerName = Bukkit.getOfflinePlayer(playerUUID).getName();

            lines.add(MessageFormat.format("{0}# {1}: {2}",
                    getColor(index) + index,
                    ChatColor.GRAY + playerName,
                    ChatColor.DARK_PURPLE + convertTime(time)));

            index++;
        }

        String leaderboardText = String.join("\n", lines);

        textDisplayPlayTime.setText(Component.text(leaderboardText).content());
    }

    public static void removeLeaderboardPlayTime() {
        if (textDisplayPlayTime != null && !textDisplayPlayTime.isDead()) {
            textDisplayPlayTime.remove();
            textDisplayPlayTime = null;
        }
    }

    public static void setTimePlayed(Player player, long time) {
        try {
            String sql = "UPDATE playtime SET time = ? WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, time);
            statement.setString(2, player.getUniqueId().toString());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                insertTimePlayed(player, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertTimePlayed(Player player, long time) {
        try {
            String sql = "INSERT INTO playtime (uuid, time) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player.getUniqueId().toString());
            statement.setLong(2, time);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Map.Entry<UUID, Long>> getTopPlayTime(int limit) {
        List<Map.Entry<UUID, Long>> playTime = new ArrayList<>();
        String sql = "SELECT uuid, time FROM playtime ORDER BY time DESC LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                UUID playerUUID = UUID.fromString(rs.getString("uuid"));
                long time = rs.getLong("time");

                playTime.add(new AbstractMap.SimpleEntry<>(playerUUID, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playTime;
    }

    public static String convertTime(long ticks) {
        long millis = ticks * 50;

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;

        return String.format("%dj %dh %dm %ds", days, hours, minutes, seconds);
    }
}
