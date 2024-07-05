package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import fr.communaywen.core.utils.database.DatabaseConnection;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    // Laissez ce truc
    // Zeub - By Xernas 05/07/2024 à 00:05 (UTC+2)
    // Le createur de ce truc

    // Vraiment bancale mon intégration d'la db + il y a la moitié fait par copilot - By ri1_ 05/07/2024 à 22:50 (UTC+2)

    private final Queue<UUID, Team> pendingInvites = new Queue<>(20);

    final DatabaseConnection databaseConnection = AywenCraftPlugin.getInstance().getDatabaseManager().getTeamConnection();

    public List<Team> getTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT owner, name, players FROM teams");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UUID owner = UUID.fromString(resultSet.getString("owner"));
                String name = resultSet.getString("name");
                List<UUID> players = parsePlayerList(resultSet.getString("players"));
                Team team = new Team(owner, name);
                players.forEach(team::addPlayer);
                teams.add(team);
            }
        }
        return teams;
    }

    public Team createTeam(UUID owner, String name) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO teams (owner, name, players) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, owner.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, owner.toString()); // Initial player is the owner

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                Team team = new Team(owner, name);
                team.addPlayer(owner);
                return team;
            }
        }
        return null;
    }

    private List<UUID> parsePlayerList(String playerListString) {
        if (playerListString == null || playerListString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(playerListString.split(","))
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    public boolean invite(UUID player, Team team) {
        if (team.getPlayers().size() >= 20) {
            return false;
        }
        pendingInvites.add(player, team);
        return true;
    }

    public Team isInTeam(UUID player) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT owner, name, players FROM teams WHERE players LIKE ?")) {

            preparedStatement.setString(1, "%" + player.toString() + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UUID owner = UUID.fromString(resultSet.getString("owner"));
                    String name = resultSet.getString("name");
                    List<UUID> players = parsePlayerList(resultSet.getString("players"));
                    Team team = new Team(owner, name);
                    players.forEach(team::addPlayer);
                    return team;
                }
            }
        }
        return null;
    }

    public Team acceptInvite(UUID player) throws SQLException {
        Team team = pendingInvites.get(player);
        if (team != null) {
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement updatePlayers = connection.prepareStatement("UPDATE teams SET players = ? WHERE name = ?")) {

                team.addPlayer(player);
                String playerListString = team.getPlayers().stream()
                        .map(UUID::toString)
                        .collect(Collectors.joining(","));

                updatePlayers.setString(1, playerListString);
                updatePlayers.setString(2, team.getName());
                updatePlayers.executeUpdate();

                pendingInvites.remove(player);
            }
        }
        return team;
    }

    public Team getTeamByName(String name) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT owner, players FROM teams WHERE name = ?")) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UUID owner = UUID.fromString(resultSet.getString("owner"));
                    List<UUID> players = parsePlayerList(resultSet.getString("players"));
                    Team team = new Team(owner, name);
                    players.forEach(team::addPlayer);
                    return team;
                }
            }
        }
        return null;
    }


    public boolean deleteTeam(Team team) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM teams WHERE name = ?")) {

            preparedStatement.setString(1, team.getName());

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                // Remove any pending invites for this team
                Iterator<Map.Entry<UUID, Team>> iterator = pendingInvites.getQueue().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Team> entry = iterator.next();
                    if (entry.getValue().equals(team)) {
                        iterator.remove();
                    }
                }
                return true;
            }
        }
        return false;
    }
}




