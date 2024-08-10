package fr.communaywen.core.commands.teleport;

import fr.communaywen.core.AywenCraftPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;


public class SpawnCommand {
    private final AywenCraftPlugin plugin;

    // Configuration values
    private final double x;
    private final double y;
    private final double z;
    private final String WORLD;

    public SpawnCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        // Load configuration values
        x = plugin.getConfig().getDouble("spawn.x");
        y = plugin.getConfig().getInt("spawn.y");
        z = plugin.getConfig().getInt("spawn.z");
        WORLD = plugin.getConfig().getString("spawn.world");
    }

    @Command("spawn")
    @Description("Se téléporter au spawn")
    @CommandPermission("ayw.command.spawn")
    public void spawn(Player player) {
        Location spawn = new Location(player.getServer().getWorld(WORLD), x, y, z, 0, 0);
        player.sendTitle(PlaceholderAPI.setPlaceholders(player, "§0%img_screeneffect%"), "§a§lTéléportation au spawn...", 20, 10, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(spawn);
            }
        }.runTaskLater(AywenCraftPlugin.getInstance(), 10);
    }

}
