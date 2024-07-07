package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class CommandSpawn implements CommandExecutor {
    private final AywenCraftPlugin plugin;

    // Configuration values
    private final double X;
    private final double Y;
    private final double Z;
    private final String WORLD;

    public CommandSpawn(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        // Load configuration values
        X = plugin.getConfig().getDouble("spawn.x");
        Y = plugin.getConfig().getInt("spawn.y");
        Z = plugin.getConfig().getInt("spawn.z");
        WORLD = plugin.getConfig().getString("spawn.world");

    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            Location spawn = new Location(player.getServer().getWorld("WORLD"),X,Y,Z,0,0);
            player.sendTitle("§aTéléportation au spawn...","",0,20,10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(spawn);
                }
            }.runTaskLater(AywenCraftPlugin.getInstance(),10);
            return true;
        }
        return false;
    }
}
