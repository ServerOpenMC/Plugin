package fr.communaywen.core.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EconomieTeam extends DatabaseConnector {

    private static final Map<String, Double> teamBalances = new HashMap<>();

    public static void loadBalances() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT teamName, balance FROM teams");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String teamName = rs.getString("teamName");
            Double balance = rs.getDouble("balance");
            teamBalances.put(teamName, balance);
        }
    }

    public static Double getTeamBalances(String teamName) {
        return teamBalances.getOrDefault(teamName, 0.0);
    }

    public static void setTeamBalances(String teamName, double balance) {
        teamBalances.put(teamName, balance);
    }

    public static void addBalance(String teamName, double amount) {
        double currentBalance = getTeamBalances(teamName);
        setTeamBalances(teamName, currentBalance + amount);
        saveBalance(teamName);
    }

    public static boolean removeBalance(String teamName, double amount) {
        double currentBalance = getTeamBalances(teamName);
        if (currentBalance >= amount && amount > 0) {
            setTeamBalances(teamName, currentBalance - amount);
            saveBalance(teamName);
            return true;
        }
        return false;
    }

    public static void saveBalance(String teamName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE teams SET balance = ? WHERE teamName = ?");
            statement.setDouble(1, getTeamBalances(teamName));
            statement.setString(2, teamName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
