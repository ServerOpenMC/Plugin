package fr.communaywen.core.contest;


import fr.communaywen.core.utils.database.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ContestManager extends DatabaseConnector {
    public static int getInt(String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contest");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 999;
    }

    public static ResultSet getTradeSelected(boolean bool) {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM contest_trades WHERE selected = "+ bool +" ORDER BY RAND() LIMIT 12");
            ResultSet rs = query.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contest");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static void updateColumnInt(String table, String column, int value) {
        String sql = "UPDATE " + table + " SET " + column + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateColumnBooleanFromRandomTrades(Boolean bool, String ress) {
        String sql = "UPDATE contest_trades SET selected = ? WHERE ress = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, bool);
            stmt.setString(2, ress);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertChoicePlayer(Player player, Integer camp) {
        String sql = "INSERT INTO camps (minecraft_uuid, camps) VALUES (?, ?)";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            states.setInt(2, camp);
            states.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DayOfWeek getCurrentDayOfWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);

        LocalDate currentDate = LocalDate.now();
        String currentDayString = currentDate.format(formatter);

        //conversion ex ven. => FRIDAY
        return DayOfWeek.from(formatter.parse(currentDayString));
    }

    public static Integer getPlayerCamp(Player player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt("camps");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static String getPlayerCampName(Player player) {
        Integer campInteger = ContestManager.getPlayerCamp(player);
        String campName = ContestManager.getString("camp" + campInteger);
        return campName;
    }

    public static ChatColor getPlayerCampChatColor(Player player) {
        Integer campInteger = ContestManager.getPlayerCamp(player);
        String color = ContestManager.getString("color" + campInteger);
        ChatColor campColor = ChatColor.valueOf(color);
        return campColor;
    }
}
