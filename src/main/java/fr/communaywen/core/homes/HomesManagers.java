package fr.communaywen.core.homes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.communaywen.core.homes.menu.utils.HomeIcons;
import fr.communaywen.core.homes.menu.utils.HomeMenuUtils;
import org.bukkit.Location;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;

public class HomesManagers extends DatabaseConnector {

    public static List<Home> homes = new ArrayList<>();
    public static List<HomeLimit> homeLimits = new ArrayList<>();

    public void addHome(Home home) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO homes (player, name, location, icon) VALUES (?, ?, ?, null)");
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

    public void changeIcon(Home home, HomeIcons icon) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE homes SET icon = ? WHERE player = ? AND name = ? AND location = ?");
            statement.setString(1, icon.getId());
            statement.setString(2, home.getPlayer());
            statement.setString(3, home.getName());
            statement.setString(4, home.serializeLocation());
            statement.executeUpdate();

            home.setIcon(icon);

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

    public static List<Home> getHomes(UUID uuid)
    {
        return homes.stream()
                .filter(home -> home.getPlayer().equals(uuid.toString()))
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
                String iconId = rs.getString("icon");

                homes.add(new Home(player, name, location, iconId == null ? HomeIcons.DEFAULT : HomeMenuUtils.getHomeIcon(iconId)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadHomeLimits() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID player = UUID.fromString(rs.getString("player"));
                int limit = rs.getInt("homes_limit");

                homeLimits.add(new HomeLimit(player, limit));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createHomeLimit(UUID playerId, int limit) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO players (player, homes_limit) VALUES (?, ?)");
            statement.setString(1, playerId.toString());
            statement.setInt(2, limit);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHomeLimit(UUID playerId, int newLimit) {
        HomeLimit homeLimit = homeLimits.stream()
                .filter(hl -> hl.getPlayerUUID().equals(playerId))
                .findFirst()
                .orElse(null);
        if (homeLimit != null) {
            homeLimit.setLimit(newLimit);
        } else {
            homeLimits.add(new HomeLimit(playerId, newLimit));
        }
    }

    public void saveHomesLimits() {
        try {
            connection.setAutoCommit(false);
            PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM players WHERE player = ?");
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE players SET homes_limit = ? WHERE player = ?");

            for(HomeLimit homeLimit : homeLimits) {
                String playerUUID = homeLimit.getPlayerUUID().toString();
                int limit = homeLimit.getLimit();

                selectStmt.setString(1, playerUUID);
                ResultSet rs = selectStmt.executeQuery();

                if(rs.next()) {
                    updateStmt.setInt(1, limit);
                    updateStmt.setString(2, playerUUID);
                    updateStmt.addBatch();
                    AywenCraftPlugin.getInstance().getLogger().info("Updating homes limit for " + playerUUID + " to " + limit);
                } else {
                    createHomeLimit(homeLimit.getPlayerUUID(), limit);
                }
                rs.close();
            }

            updateStmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

            selectStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                AywenCraftPlugin.getInstance().getLogger().severe("Error during rollback: " + rollbackEx.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    public int getHomeLimit(UUID uuid) {
        HomeLimit homeLimit = homeLimits.stream()
                .filter(hl -> hl.getPlayerUUID().equals(uuid))
                .findFirst()
                .orElse(null);
        if (homeLimit != null)
            return homeLimit.getLimit();
        return HomeUpgrade.UPGRADE_1.getHomes();
    }
}
