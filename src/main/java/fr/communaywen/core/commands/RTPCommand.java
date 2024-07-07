package fr.communaywen.core.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.UUID;

public class RTPCommand {
	private final AywenCraftPlugin plugin;

    // Configuration values
    private final int COOLDOWN_TIME;
    private final int COOLDOWN_ERROR;
    private final int MIN_X;
    private final int MAX_X;
    private final int MIN_Z;
    private final int MAX_Z;
    private int MAX_TRY;

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
        MAX_TRY = plugin.getConfig().getInt("rtp.max_try");
	    
    }

    @SuppressWarnings("deprecation")
	@Command({ "rtp", "randomteleportation" })
    @Description("Téléportation aléatoire")
    @CommandPermission("ayw.command.rtp")
    public void onCommand(Player player) {
        UUID playerId = player.getUniqueId();
        long Time = System.currentTimeMillis() / 1000;
        long ExactTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerId)) {
            long lastUsed = cooldowns.get(playerId);
            long timeSinceLastUse = Time - lastUsed;

            if (timeSinceLastUse < COOLDOWN_TIME) {
                long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                return;
            }
        }
        World world = player.getWorld();
        for (int i=1; i<=MAX_TRY;i++) {
            int x = (int) (Math.random() * (MAX_X - MIN_X) + MIN_X);
            int z = (int) (Math.random() * (MAX_Z - MIN_Z) + MIN_Z);
            if (!world.getBiome(new Location(world, x, 64, z)).equals(Biome.RIVER) || !world.getBiome(new Location(world, x, 64, z)).toString().contains("OCEAN")) {
                int y = world.getHighestBlockAt(new Location(world, x, 64, z)).getY();
                Location location = new Location(world, x, y+1, z);
                if (new Location(world, x, y, z).getBlock().getType().isSolid()){
                    player.teleport(location);
                    player.sendMessage(" §aRTP réussi x: §f" + x + " §ay: §f" + y + " §az: §f" + z );
                    player.sendTitle(" §aRTP réussi", "x: " + x + " y: " + y + " z: " + z );
                    cooldowns.put(playerId, Time);
                    return;
                }
            }
            if (i==3){
                player.sendTitle(" §cErreur","/rtp");
                cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
            }
        }
    }
}
