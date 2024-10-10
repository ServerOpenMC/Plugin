package fr.communaywen.core.spawn.jump;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.utils.database.DatabaseConnector;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class JumpManager extends DatabaseConnector {
    AywenCraftPlugin plugin;
    FileConfiguration config;

    public JumpManager(AywenCraftPlugin plugins) {
        config = plugins.getConfig();
        plugin = plugins;
    }

    private static Map<Player, Long> jumpStartTimes = new HashMap<>();

    public void startJump(Player player) {
        long startTime = System.currentTimeMillis();
        jumpStartTimes.put(player, startTime);
    }

    public double endJump(Player player) {
        if (!jumpStartTimes.containsKey(player)) {
            return -1;
        }

        long startTime = jumpStartTimes.get(player);

        jumpStartTimes.remove(player);

        long endTime = System.currentTimeMillis();
        double elapsedSeconds = (endTime - startTime) / 1000.0;

        return elapsedSeconds;
    }

    public double getElapsedSeconds(Player player) {
        long startTime = jumpStartTimes.get(player);

        long currentTime = System.currentTimeMillis();
        double elapsedSeconds = (currentTime - startTime) / 1000.0;

        return elapsedSeconds;
    }

    public double getBestTime(Player player) {
        double bestTime = -1;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT best_time FROM spawn_jump WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bestTime = resultSet.getDouble("best_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bestTime;
    }

    public void setBestTime(Player player, double bestTime) {
        try {
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO spawn_jump (uuid, best_time) VALUES (?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, bestTime);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateActionBar(Player player, double time) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent("» §2Jump §6§l" + time + "s §2passés sur le Jump"));
    }

    public static boolean isJumping(Player player) {
        return jumpStartTimes.containsKey(player);
    }

    private TextDisplay textDisplayJumpStart;

    public void createDisplayJumpStart() {
        World world = Bukkit.getWorld((String) config.get("jump.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("jump.start.posX")+0.5, config.getDouble("jump.start.posY")+1, config.getDouble("jump.start.posZ")+0.5);

        textDisplayJumpStart = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayJumpStart.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayJumpStart.setViewRange(100.0F);
        textDisplayJumpStart.setDefaultBackground(false);
        textDisplayJumpStart.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayJumpStart.setCustomNameVisible(false);
        textDisplayJumpStart.setCustomName("jump_start");
        textDisplayJumpStart.setText(Component.text("§dDébut §f- §dJump").content());
    }

    private TextDisplay textDisplayJumpEnd;
    public void createDisplayJumpEnd() {
        World world = Bukkit.getWorld((String) config.get("jump.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("jump.end.posX")+0.5, config.getDouble("jump.end.posY")+1, config.getDouble("jump.end.posZ")+0.5);

        textDisplayJumpEnd = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayJumpEnd.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayJumpEnd.setViewRange(100.0F);
        textDisplayJumpEnd.setDefaultBackground(false);
        textDisplayJumpEnd.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayJumpEnd.setCustomNameVisible(false);
        textDisplayJumpEnd.setCustomName("jump_start");
        textDisplayJumpEnd.setText(Component.text("§dFin §f- §dJump").content());
    }

    public void removeDisplayJumpStart() {
        if ((textDisplayJumpStart != null) && !textDisplayJumpStart.isDead()) {
            textDisplayJumpStart.remove();
            textDisplayJumpStart = null;
        }
    }
    public void removeDisplayJumpEnd() {
        if ((textDisplayJumpEnd != null) && !textDisplayJumpEnd.isDead()) {
            textDisplayJumpEnd.remove();
            textDisplayJumpEnd = null;
        }
    }


}
