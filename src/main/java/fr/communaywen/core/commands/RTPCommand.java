package fr.communaywen.core.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class RTPCommand implements CommandExecutor {

	private final AywenCraftPlugin plugin;

    // Configuration values
    private final int COOLDOWN_TIME;
    private final int COOLDOWN_ERROR;
    private final int MIN_X;
    private final int MAX_X;
    private int MIN_Y;
    private int MAX_Y;
    private final int MIN_Z;
    private final int MAX_Z;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private final HashMap<UUID, Location> loc = new HashMap<>();

    public RTPCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        
        // Load configuration values
        COOLDOWN_TIME = plugin.getConfig().getInt("rtp.cooldown");
        COOLDOWN_ERROR = plugin.getConfig().getInt("rtp.cooldown-error");
        MIN_X = plugin.getConfig().getInt("rtp.minx");
        MAX_X = plugin.getConfig().getInt("rtp.maxx");
        MIN_Z = plugin.getConfig().getInt("rtp.minz");
        MAX_Z = plugin.getConfig().getInt("rtp.maxz");
	    plugin.getConfig().options().copyDefaults(true);
	    plugin.saveConfig();
	    
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(@NotNull final CommandSender sender,@NotNull final Command command,@NotNull final String label, final String[] args) {
        if (sender instanceof Player player) {
            UUID playerId = player.getUniqueId();
            long Time = System.currentTimeMillis() / 1000;
            long ExactTime = System.currentTimeMillis();

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = Time - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    return true;
                }
            }
            World world = player.getWorld();
            int x = (int) (Math.random() * (MAX_X - MIN_X) + MIN_X);
            int z = (int) (Math.random() * (MAX_Z - MIN_Z) + MIN_Z);
            if (!world.getBiome(new Location(world, x, 64, z)).equals(Biome.RIVER) || !world.getBiome(new Location(world, x, 64, z)).toString().contains("OCEAN")) {
                int y = world.getHighestBlockAt(new Location(world, x, 64, z)).getY();
                Location location = new Location(world, x, y+1, z);
                if (new Location(world, x, y, z).getBlock().getType().isSolid()){
                    player.teleport(location);
                    player.sendTitle(" §aRTP réussi", "x: " + x + " y: " + y + " z: " + z + " " + (System.currentTimeMillis() - ExactTime)/1000 + "s");
                    cooldowns.put(playerId, Time);
                    return true;
                }
                else{
                    player.sendTitle(" §cErreur","/rtp");
                    cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
                    return true;
                }
            }
            player.sendTitle(" §cErreur","/rtp");
            cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
        }

        return true;
    }
}
