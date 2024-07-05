package fr.communaywen.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.misieur.AywenCraftPlugin.AywenCraftPlugin;

import java.util.HashMap;
import java.util.UUID;

public class RTPCommand implements CommandExecutor {

	private final AywenCraftPlugin plugin;

    // Configuration values
    private final int COOLDOWN_TIME;
    private final int COOLDOWN_ERROR;
    private final int MIN_X;
    private final int MAX_X;
    private final int MIN_Y;
    private final int MAX_Y;
    private final int MIN_Z;
    private final int MAX_Z;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public RTPCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        // Load configuration values
        COOLDOWN_TIME = plugin.getConfig().getInt("rtp.cooldown");
        COOLDOWN_ERROR = plugin.getConfig().getInt("rtp.cooldown-error");
        MIN_X = plugin.getConfig().getInt("rtp.minx");
        MAX_X = plugin.getConfig().getInt("rtp.maxx");
        MIN_Y = plugin.getConfig().getInt("rtp.miny");
        MAX_Y = plugin.getConfig().getInt("rtp.maxy");
        MIN_Z = plugin.getConfig().getInt("rtp.minz");
        MAX_Z = plugin.getConfig().getInt("rtp.maxz");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player player) {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis() / 1000;

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = currentTime - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                    return true;
                }
            }

            World world = player.getWorld();
            int x = (int) (Math.random() * (MAX_X - MIN_X) + MIN_X);
            int z = (int) (Math.random() * (MAX_Z - MIN_Z) + MIN_Z);

            for (int y = MIN_Y; y <= MAX_Y; y++) {
                Location location = new Location(world, x, y, z);
                Location belowLocation = new Location(world, x, y - 1, z);

                if (location.getBlock().getType().isAir() && belowLocation.getBlock().getType().isSolid()) {
                    player.teleport(location);
                    player.sendTitle(" §aRTP réussi", "x: " + x + " y: " + y + " z: " + z);
                    cooldowns.put(playerId, currentTime);
                    return true;
                }
            }

            player.sendTitle(" §cErreur","/rtp"));
            cooldowns.put(playerId, currentTime - COOLDOWN_TIME + COOLDOWN_ERROR);
            return true;
        }

        return true;
    }
}
