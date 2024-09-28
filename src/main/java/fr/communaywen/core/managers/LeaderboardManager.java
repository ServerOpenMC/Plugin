package fr.communaywen.core.managers;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.commands.economy.BaltopCommand;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.TextDisplay;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {
    /**
    static FileConfiguration config;
    static AywenCraftPlugin plugins;
    public LeaderboardManager(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;
    }
    private static TextDisplay textDisplay;

    public static void createLeaderboard() {
        System.out.println("Creation du LB");
        World world = Bukkit.getWorld((String) config.get("leaderboard.baltop.world"));
        if (world == null) return;

        Location location = new Location(world, config.getDouble("leaderboard.baltop.posX"), config.getDouble("leaderboard.baltop.posY"), config.getDouble("leaderboard.baltop.posZ"));

        textDisplay = (TextDisplay) world.spawn(location, TextDisplay.class);

        textDisplay.setBillboard(TextDisplay.Billboard.CENTER);
        textDisplay.setViewRange(100.0F);
        textDisplay.setBackgroundColor(org.bukkit.Color.fromRGB(0, 0, 0));
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);

        textDisplay.setCustomNameVisible(true);
        textDisplay.setCustomName("§a§lClassement des meilleurs joueurs (en money)");
    }

    public static void updateLeaderboard() {
        System.out.println("update");
        if (textDisplay == null) return;


        textDisplay.setText(Test);
    }
    **/
}
