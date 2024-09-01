package fr.communaywen.core.claim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClaimConfigDataBase extends DatabaseConnector {

    private static List<Map<String, Object>> tempClaims = new ArrayList<>();

    public static void loadAllClaimsData() {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM claim");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Map<String, Object> tempClaim = new HashMap<>();
                tempClaim.put("claimID", result.getString("claimID"));
                tempClaim.put("team", result.getString("team"));
                tempClaim.put("pos1X", result.getDouble("pos1X"));
                tempClaim.put("pos1Z", result.getDouble("pos1Z"));
                tempClaim.put("pos2X", result.getDouble("pos2X"));
                tempClaim.put("pos2Z", result.getDouble("pos2Z"));
                tempClaim.put("world", result.getString("world"));
                tempClaim.put("claimer", result.getString("claimer"));
                tempClaims.add(tempClaim);
            }

        } catch (SQLException ignored) {}
    }

    public static void processStoredClaimData() {
        for(Map<String, Object> claimData : tempClaims) {
            Location pos1 = new Location(Bukkit.getWorld((String) claimData.get("world")), (Double) claimData.get("pos1X"), -62, (Double) claimData.get("pos1Z"));
            Location pos2 = new Location(Bukkit.getWorld((String) claimData.get("world")), (Double) claimData.get("pos2X"), 320, (Double) claimData.get("pos2Z"));
            Team team = AywenCraftPlugin.getInstance().getManagers().getTeamManager().getTeamByName((String) claimData.get("team"));
            String uuidString = (String) claimData.get("claimID");
            String playerUUIDString = (String) claimData.get("claimer");

            if (playerUUIDString == null || playerUUIDString.isEmpty() || playerUUIDString.isBlank()) {
                if (team != null) {
                    playerUUIDString = team.getOwner().toString();
                } else {
                    continue;
                }
            }

            try {
                UUID uuid = UUID.fromString(uuidString);
                UUID playerUUID = UUID.fromString(playerUUIDString);
                if (team != null) {
                    AywenCraftPlugin.getInstance().regions.add(new RegionManager(pos1, pos2, team, uuid, playerUUID));
                }
            } catch (IllegalArgumentException e) {
                AywenCraftPlugin.getInstance().getLogger().severe("Invalid UUID string: " + uuidString + " or " + playerUUIDString);
            }
        }
        tempClaims.clear();
    }

    public static boolean addClaims(UUID claimID, String teamName, Double pos1X, Double pos1Z, Double pos2X, Double pos2Z, String world, UUID claimer) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO claim (claimID, team, pos1x, pos1z, pos2x, pos2z, world, claimer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, String.valueOf(claimID));
            statement.setString(2, teamName);
            statement.setDouble(3, pos1X);
            statement.setDouble(4, pos1Z);
            statement.setDouble(5, pos2X);
            statement.setDouble(6, pos2Z);
            statement.setString(7, world);
            statement.setString(8, String.valueOf(claimer));

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            AywenCraftPlugin.getInstance().getLogger().info(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static boolean removeClaims(String teamName, UUID claimID) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM claim WHERE team = ? AND claimID = ?");

            statement.setString(1, teamName);
            statement.setString(2, String.valueOf(claimID));

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean removeAllClaims(String teamName) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM claim WHERE team = ?");
            statement.setString(1, teamName);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                AywenCraftPlugin.getInstance().regions.removeIf(region -> region.getTeam().getName().equals(teamName));
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getCountClaims(String teamName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM claim WHERE team = ?");

            statement.setString(1, teamName);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                return result.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return -1;
        }
    }
}