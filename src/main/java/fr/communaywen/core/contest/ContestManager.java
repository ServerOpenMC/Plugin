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
import java.util.List;
import java.util.Locale;

import static org.bukkit.Bukkit.getOfflinePlayers;

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

    public static OfflinePlayer getPlayerOffline(String uuid) {
        for(OfflinePlayer player : getOfflinePlayers()) {
            if(player.getUniqueId().equals(uuid)) return player;
        }
        return null;
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
}
