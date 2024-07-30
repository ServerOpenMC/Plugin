ah merde
    package fr.communaywen.core.quests;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.utils.database.DatabaseConnector;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.*;

public class QuestsManager extends DatabaseConnector {

    private static final Map<UUID, PlayerQuests> playerQuests = new HashMap<>();

    public static void createQuestsTable() throws SQLException {

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet tables = meta.getTables(null, null, "quests", null);

        if (!tables.next()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS quests (player VARCHAR(36) PRIMARY KEY");

            for (QUESTS quest : QUESTS.values()) {
                sql.append(", ").append(quest.name()).append(" INT DEFAULT 0");
            }

            sql.append(")");

            Statement stmt = connection.createStatement();
            stmt.execute(sql.toString());
            stmt.close();
            System.out.println("Table 'quests' created successfully.");
        } else {
            for (QUESTS quest : QUESTS.values()) {
                if (!columnExists(quest.name())) {
                    addColumn(quest.name());
                }
            }
        }

        tables.close();
    }

    public static void loadPlayerData(Player player) throws SQLException {
        UUID playerId = player.getUniqueId();
        PlayerQuests pq = new PlayerQuests();

        for (QUESTS quest : QUESTS.values()) {
            if (!columnExists(quest.name())) {
                addColumn(quest.name());
            }
        }

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM quests WHERE player = ?");
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

        rs.close();
        statement.close();

        playerQuests.put(playerId, pq);
    }

    private static void insertNewPlayer(UUID playerId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO quests (player) VALUES (?)");
        stmt.setString(1, playerId.toString());

        stmt.executeUpdate();
        stmt.close();
    }

    private static boolean columnExists(String columnName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rs = meta.getColumns(null, null, "quests", columnName);
        boolean exists = rs.next();
        rs.close();
        return exists;
    }

    private static void addColumn(String columnName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("ALTER TABLE quests ADD COLUMN " + columnName + " INT DEFAULT 0");
        stmt.close();
    }

    public static PlayerQuests getPlayerQuests(Player player) {
        return playerQuests.get(player.getUniqueId());
    }

    public static void manageQuestsPlayer(Player player, QUESTS quests, int amount, String actionBar) {
        PlayerQuests pq = getPlayerQuests(player);

        try {
            if (!pq.isQuestCompleted(quests)) {
                pq.addProgress(quests, amount);
                savePlayerQuestProgress(player, quests, pq.getProgress(quests));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("» §7[§9§lQuêtes§7] §6" + pq.getProgress(quests) + "§l/§6" + quests.getQt() + " " + actionBar));
                if (pq.isQuestCompleted(quests)) {
                    player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 10);
                    player.sendTitle("§6QUETE COMPLETE", "§e" + quests.getName());
                    player.sendMessage("» §7[§9§lQuêtes§7] §6Quête complétée: §e" + quests.getName());
                    switch (quests.getReward()) {
                        case ITEMS -> {
                            player.getInventory().addItem(new ItemStack(quests.getRewardsMaterial().getType(), quests.getRewardsQt()));
                            player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quests.getRewardsQt() + " " + quests.getRewardsMaterial().getType().name());
                        }
                        case MONEY -> {
                            AywenCraftPlugin.getInstance().getManagers().getEconomyManager().addBalance(player, quests.getRewardsQt());
                            player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quests.getRewardsQt() + "$");
                        }
                    }
                }
            }
        } catch (SQLException ignored) {}
    }
    public static void savePlayerQuestProgress(Player player, QUESTS quest, int progress) throws SQLException {
        CompletableFuture.runAsync(() -> {
        UUID playerId = player.getUniqueId();
        String sql = "UPDATE quests SET " + quest.name() + " = ? WHERE player = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, progress);
        stmt.setString(2, playerId.toString());
        stmt.executeUpdate();
        stmt.close();
        });
    }
}
