package fr.communaywen.core.claim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ErrorManager;

public class ClaimConfigDataBase extends DatabaseConnector {

    public static boolean loadAllClaims() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM claims");


            ResultSet result = statement.executeQuery();
            if(result.next()) {
                Location pos1 = new Location(Bukkit.getWorld(result.getString("world")), result.getDouble("pos1X"), -62, result.getDouble("pos1Z"));
                Location pos2 = new Location(Bukkit.getWorld(result.getString("world")), result.getDouble("pos2X"), 320, result.getDouble("pos2Z"));
                Team team = AywenCraftPlugin.getInstance().getManagers().getTeamManager().getTeamByName(result.getString("team"));
                if (team != null) {
                    AywenCraftPlugin.getInstance().regions.add(new RegionManager(pos1, pos2, team));
                    AywenCraftPlugin.getInstance().getLogger().info(AywenCraftPlugin.getInstance().regions.toString());
                }
                AywenCraftPlugin.getInstance().getLogger().info(result.getString("team"));
            } else {
                AywenCraftPlugin.getInstance().getLogger().info("Une erreur est survenu avec la base de donnée.");
            }


            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addClaims(String teamName, Double pos1X, Double pos1Z, Double pos2X, Double pos2Z, String world) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO claim (team, pos1x, pos1z, pos2x, pos2z, world) VALUES (?, ?, ?, ?, ?, ?)");

            statement.setString(1, teamName);
            statement.setDouble(2, pos1X);
            statement.setDouble(3, pos1Z);
            statement.setDouble(4, pos2X);
            statement.setDouble(5, pos2Z);
            statement.setString(6, world);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            AywenCraftPlugin.getInstance().getLogger().info(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static boolean removeClaims(String teamName) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE INTO claim WHERE team = ?");

            statement.setString(1, teamName);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
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