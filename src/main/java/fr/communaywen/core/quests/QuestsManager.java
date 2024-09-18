package fr.communaywen.core.quests;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.utils.Transaction;
import fr.communaywen.core.utils.database.DatabaseConnector;
import fr.communaywen.core.utils.database.TransactionsManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.*;

public class QuestsManager extends DatabaseConnector {

    private static final Map<UUID, PlayerQuests> playerQuests = new HashMap<>();
    private static final String TABLE_NAME = "quests";
    private static final String PLAYER_COLUMN = "player";

    public static void initializeQuestsTable() throws SQLException {
        if (!tableExists()) {
            createQuestsTable();
        } else {
            updateQuestsTable();
        }
    }

    public static void loadPlayerData(Player player) throws SQLException {
        UUID playerId = player.getUniqueId();
        PlayerQuests pq = new PlayerQuests();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + PLAYER_COLUMN + " = ?")) {
            statement.setString(1, playerId.toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                for (QUESTS quest : QUESTS.values()) {
                    int progress = rs.getInt(quest.name());
                    pq.getQuestsProgress().put(quest, progress);
                }
            } else {
                insertNewPlayer(playerId);
            }
        }

        playerQuests.put(playerId, pq);
    }

    public static PlayerQuests getPlayerQuests(Player player) {
        return playerQuests.get(player.getUniqueId());
    }
    public static PlayerQuests getPlayerQuestsOffline(OfflinePlayer player) {
        return playerQuests.get(player.getUniqueId());
    }

    public static void manageQuestsPlayer(Player player, QUESTS quest, int amount, String actionBar) {
        PlayerQuests pq = getPlayerQuests(player);

        if (!pq.isQuestCompleted(quest)) {
            pq.addProgress(quest, amount);
            sendActionBar(player, quest, pq.getProgress(quest), actionBar);

            if (pq.isQuestCompleted(quest)) {
                completeQuest(player, quest);
            }
        }
    }

    public static void savePlayerQuestProgress(Player player, QUESTS quest, int progress) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET " + quest.name() + " = ? WHERE " + PLAYER_COLUMN + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, progress);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        }
    }

    private static boolean tableExists() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet tables = meta.getTables(null, null, TABLE_NAME, null)) {
            return tables.next();
        }
    }

    private static void createQuestsTable() throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + PLAYER_COLUMN + " VARCHAR(36) PRIMARY KEY");

        for (QUESTS quest : QUESTS.values()) {
            sql.append(", ").append(quest.name()).append(" INT DEFAULT 0");
        }

        sql.append(")");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql.toString());
        }
        System.out.println("Table '" + TABLE_NAME + "' created successfully.");
    }

    private static void updateQuestsTable() throws SQLException {
        for (QUESTS quest : QUESTS.values()) {
            if (!columnExists(quest.name())) {
                addColumn(quest.name());
            }
        }
    }

    private static void insertNewPlayer(UUID playerId) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + PLAYER_COLUMN + ") VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerId.toString());
            stmt.executeUpdate();
        }
    }

    private static boolean columnExists(String columnName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, TABLE_NAME, columnName)) {
            return rs.next();
        }
    }

    private static void addColumn(String columnName) throws SQLException {
        String sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + columnName + " INT DEFAULT 0";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void sendActionBar(Player player, QUESTS quest, int progress, String actionBar) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent("» §7[§9§lQuêtes§7] §6" + progress + "§l/§6" + quest.getQt() + " " + actionBar));
    }

    private static void completeQuest(Player player, QUESTS quest) {
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 10);
        player.sendTitle("§6QUETE COMPLETE", "§e" + quest.getName());
        player.sendMessage("» §7[§9§lQuêtes§7] §6Quête complétée: §e" + quest.getName());

        switch (quest.getReward()) {
            case ITEMS:
                player.getInventory().addItem(new ItemStack(quest.getRewardsMaterial().getType(), quest.getRewardsQt()));
                player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quest.getRewardsQt() + " " + quest.getRewardsMaterial().getType().name());
                break;
            case MONEY:
                AywenCraftPlugin.getInstance().getManagers().getEconomyManager().addBalance(player, quest.getRewardsQt());


                new TransactionsManager().addTransaction(new Transaction(
                        player.getUniqueId().toString(),
                        "CONSOLE",
                        quest.getRewardsQt(),
                        "Quest"
                ));

                player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quest.getRewardsQt() + "$");
                break;
        }
    }
}