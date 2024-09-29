package fr.communaywen.core.managers;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.commands.economy.BaltopCommand;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.TextDisplay;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static fr.communaywen.core.commands.economy.BaltopCommand.getColor;

public class LeaderboardManager {
    static FileConfiguration config;
    static AywenCraftPlugin plugins;
    public LeaderboardManager(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;
    }
    private static TextDisplay textDisplayBalTop;

    public static void createLeaderboardBalTop() {
        World world = Bukkit.getWorld((String) config.get("leaderboard.baltop.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.baltop.posX"), config.getDouble("leaderboard.baltop.posY"), config.getDouble("leaderboard.baltop.posZ"));

        textDisplayBalTop = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplayBalTop.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplayBalTop.setViewRange(100.0F);
        textDisplayBalTop.setDefaultBackground(false);
        textDisplayBalTop.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplayBalTop.setCustomNameVisible(false);
        textDisplayBalTop.setCustomName("money");
    }

    public static void updateLeaderboardBalTop() {
        if (textDisplayBalTop == null) return;
        List<BaltopCommand.PlayerBalance> balances = BaltopCommand.getBalances();
        balances.sort((a, b) -> a.balance.intValue() - b.balance.intValue());

        balances = balances.reversed();

        if (balances.size() > 10) {
            balances = balances.subList(0, 10);
        }

        List<String> lines = new ArrayList<>();
        lines.add(ChatColor.LIGHT_PURPLE + "§lListe des plus riches !");

        int index = 1;
        for (BaltopCommand.PlayerBalance playerBalance : balances) {
            String playerName = playerBalance.playerId.toString();
            if (Bukkit.getPlayer(playerBalance.playerId) != null) {
                playerName = Bukkit.getOfflinePlayer(playerBalance.playerId).getName();
            } else if (Bukkit.getOfflinePlayer(playerBalance.playerId) != null) {
                playerName = Bukkit.getOfflinePlayer(playerBalance.playerId).getName();
            }
            lines.add(MessageFormat.format("{0}# {1}: {2}", getColor(index) + index, ChatColor.GRAY + playerName, ChatColor.DARK_PURPLE + playerBalance.balance.toString()));

            index++;
        }

        String leaderboardText = String.join("\n", lines);

        textDisplayBalTop.setText(Component.text(leaderboardText).content());
    }

    public static void removeLeaderboardBalTop() {
        if (textDisplayBalTop != null && !textDisplayBalTop.isDead()) {
            textDisplayBalTop.remove();
            textDisplayBalTop = null;
        }
    }
}
