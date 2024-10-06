package fr.communaywen.core.homes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.communaywen.core.utils.constant.MessageType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.Prefix;
import fr.communaywen.core.utils.database.DatabaseConnector;

public class HomesManagers extends DatabaseConnector {

    public static List<Home> homes = new ArrayList<>();

    public void addHome(Home home) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO homes (player, name, location) VALUES (?, ?, ?)");
            statement.setString(1, home.getPlayer());
            statement.setString(2, home.getName());
            statement.setString(3, home.serializeLocation());

            homes.add(home);

            statement.executeUpdate();

        } catch (SQLException ignored) {

        }
    }

    // remove home
    public void removeHome(Home home) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM homes WHERE player = ? AND name = ? AND location = ?");
            statement.setString(1, home.getPlayer());
            statement.setString(2, home.getName());
            statement.setString(3, home.serializeLocation());

            homes.remove(home);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void renameHome(Home home, String newName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE homes SET name = ? WHERE player = ? AND name = ?");
            statement.setString(1, newName);
            statement.setString(2, home.getPlayer());
            statement.setString(3, home.getName());
            statement.executeUpdate();

            home.setName(newName);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHomeNamesByPlayer(UUID playerId) {
        return homes.stream()
                .filter(home -> home.getPlayer().equals(playerId.toString()))
                .map(Home::getName)
                .collect(Collectors.toList());
    }

    public void loadHomes() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM homes");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String player = rs.getString("player");
                String name = rs.getString("name");
                String loc = rs.getString("location");
                Location location = Home.deserializeLocation(loc);

                homes.add(new Home(player, name, location));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentHomesLimit(UUID playerId) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE player = ?");
            statement.setString(1, playerId.toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("homes_limit");
            } else {
                statement = connection.prepareStatement("INSERT INTO players (player, homes_limit) VALUES (?, ?)");
                statement.setString(1, playerId.toString());
                statement.setInt(2, AywenCraftPlugin.getInstance().getConfig().getInt("homes.default_limit"));
                statement.executeUpdate();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public void upgradeHomesLimit(UUID playerId, int newLimit) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE players SET homes_limit = ? WHERE player = ?");
            statement.setInt(1, newLimit);
            statement.setString(2, playerId.toString());
    
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                statement = connection.prepareStatement("INSERT INTO players (player, homes_limit) VALUES (?, ?)");
                statement.setString(1, playerId.toString());
                statement.setInt(2, newLimit);
                statement.executeUpdate();
            }
    
            int updatedHomesLimit = getCurrentHomesLimit(playerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
