package fr.communaywen.core.contest;


import fr.communaywen.core.utils.database.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class ContestManager extends DatabaseConnector {
    //import from axeno
    public static boolean hasEnoughItems(Player player, Material item, int amount) {
        int totalItems = 0;
        ItemStack[] contents = player.getInventory().getContents();

        for (ItemStack is : contents) {
            if (is != null && is.getType() == item) {
                totalItems += is.getAmount();
            }
        }

        if (amount == 0) return false;
        return totalItems >= amount;
    }

    public static void removeItemsFromInventory(Player player, Material item, int quantity) {
        ItemStack[] contents = player.getInventory().getContents();
        int remaining = quantity;

        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == item) {
                int stackAmount = stack.getAmount();
                if (stackAmount <= remaining) {
                    player.getInventory().setItem(i, null);
                    remaining -= stackAmount;
                } else {
                    stack.setAmount(stackAmount - remaining);
                    remaining = 0;
                }
            }
        }
    }

    //my part
    public static int getInt(String table, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+table);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 999;
    }

    public static int getIntWhere(String table, String where, String m, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+ table + " WHERE "+ where +" = ?");
            statement.setString(1, m);
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

        String sql = "INSERT INTO camps (minecraft_uuid, name, camps, point_dep) VALUES (?, ?, ?, 0)";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            states.setString(2, player.getName());
            states.setInt(3, camp);
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

    public static ResultSet getAllPlayer() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM camps");
            ResultSet rs = query.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPlayerPoints(Player player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static ChatColor getPlayerCampChatColor(Player player) {
        Integer campInteger = ContestManager.getPlayerCamp(player);
        String color = ContestManager.getString("color" + campInteger);
        ChatColor campColor = ChatColor.valueOf(color);
        return campColor;
    }
    public static String getPlayerCampName(Player player) {
        Integer campInteger = ContestManager.getPlayerCamp(player);
        String campName = ContestManager.getString("camp" + campInteger);
        return campName;
    }
    public static Integer getOfflinePlayerCamp(OfflinePlayer player) {
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
        return -1;
    }

    public static Integer getVoteTaux(Integer camps) {
        String sql = "SELECT COUNT(*) FROM camps WHERE camps = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setInt(1, camps);
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static String getOfflinePlayerCampName(OfflinePlayer player) {
        Integer campInteger = ContestManager.getOfflinePlayerCamp(player);
        String campName = ContestManager.getString("camp" + campInteger);
        return campName;
    }
    public static ChatColor getOfflinePlayerCampChatColor(OfflinePlayer player) {
        Integer campInteger = ContestManager.getOfflinePlayerCamp(player);
        String color = ContestManager.getString("color" + campInteger);
        ChatColor campColor = ChatColor.valueOf(color);
        return campColor;
    }

    public static Integer getRankPlayerInContest(OfflinePlayer player) {
        String sql = "SELECT COUNT(*) AS rank FROM camps WHERE point_dep > (SELECT point_dep FROM camps WHERE minecraft_uuid = ?);";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt("rank") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static ResultSet getAllPlayerOrdered() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM camps ORDER BY point_dep DESC");
            ResultSet rs = query.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRankContest(Player player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int points = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                points = result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(points >= 10000) {
            return "Dictateur en  ";
        } else if (points >= 2500) {
            return "Colonel en ";
        } else if (points >= 2000) {
            return "Addict en ";
        } else if (points >= 1500) {
            return "Dieu en ";
        } else if (points >= 1000) {
            return "Légende en ";
        } else if (points >= 750) {
            return "Sénior en ";
        } else if (points >= 500) {
            return "Pro en ";
        } else if (points >= 250) {
            return "Semi-Pro en ";
        } else if (points >= 100) {
            return "Amateur en ";
        } else if (points >= 0) {
            return "Noob en ";
        }

        return "";
    }

    public static int getRepPointsToRank(Player player) {
        int points = getPlayerPoints(player);

        if(points >= 10000) {
            return 0;
        } else if (points >= 2500) {
            return 10000;
        } else if (points >= 2000) {
            return 2500;
        } else if (points >= 1500) {
            return 2000;
        } else if (points >= 1000) {
            return 1500;
        } else if (points >= 750) {
            return 1000;
        } else if (points >= 500) {
            return 750;
        } else if (points >= 250) {
            return 500;
        } else if (points >= 100) {
            return 250;
        } else if (points >= 0) {
            return 100;
        }

        return 0;
    }

    public static String getRankContestFroOffline(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int points = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                points = result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(points >= 10000) {
            return "Dictateur en  ";
        } else if (points >= 2500) {
            return "Colonel en ";
        } else if (points >= 2000) {
            return "Addict en ";
        } else if (points >= 1500) {
            return "Dieu en ";
        } else if (points >= 1000) {
            return "Légende en ";
        } else if (points >= 750) {
            return "Sénior en ";
        } else if (points >= 500) {
            return "Pro en ";
        } else if (points >= 250) {
            return "Semi-Pro en ";
        } else if (points >= 100) {
            return "Amateur en ";
        } else if (points >= 0) {
            return "Noob en ";
        }

        return "";
    }

    public static int getRankContestFromOfflineInt(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int points = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                points = result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(points >= 10000) {
            return 10;
        } else if (points >= 2500) {
            return 9;
        } else if (points >= 2000) {
            return 8;
        } else if (points >= 1500) {
            return 7;
        } else if (points >= 1000) {
            return 6;
        } else if (points >= 750) {
            return 5;
        } else if (points >= 500) {
            return 4;
        } else if (points >= 250) {
            return 3;
        } else if (points >= 100) {
            return 2;
        } else if (points >= 0) {
            return 1;
        }

        return 0;
    }

    public static boolean hasWinInCampForOfflinePlayer(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int playerCamp = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                playerCamp = result.getInt("camps");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "SELECT * FROM contest";
        int points1 = 0;
        int points2 = 0;
        try (PreparedStatement states2 = connection.prepareStatement(sql2)) {
            ResultSet result = states2.executeQuery();
            if (result.next()) {
                points1 = result.getInt("points1");
                points2 = result.getInt("points2");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (points1 > points2 && playerCamp == 1) {
            return true;
        }
        if (points2 > points1 && playerCamp == 2) {
            return true;
        }
        return false;
    }

    public static int giveMoneyRandomly(Integer min, Integer max) {
        int moneyGive = new Random().nextInt(min, max);
        return moneyGive;
    }

    public static double getMultiFromRang(int rang) {
        if(rang == 10) {
            return 2.4;
        } else if (rang == 9) {
            return 2.0;
        } else if (rang == 8) {
            return 1.8;
        } else if (rang == 7) {
            return 1.7;
        } else if (rang == 6) {
            return 1.6;
        } else if (rang == 5) {
            return 1.5;
        } else if (rang == 4) {
            return 1.4;
        } else if (rang == 3) {
            return 1.3;
        } else if (rang == 2) {
            return 1.1;
        } else if (rang == 1) {
            return 1;
        }

        return 0;
    }
}
