package fr.communaywen.core.listeners;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.communaywen.core.AywenCraftPlugin;
import dev.lone.itemsadder.api.CustomStack;

public class RTPWand implements Listener {

	private final AywenCraftPlugin plugin;

    // Configuration values
    private int COOLDOWN_TIME;
    private int COOLDOWN_ERROR;
    private int MIN_X;
    private int MAX_X;
    private int MIN_Y;
    private int MAX_Y;
    private int MIN_Z;
    private int MAX_Z;
    private String RTP_WAND_NAME;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public RTPWand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        
        // Load configuration values
        COOLDOWN_TIME = plugin.getConfig().getInt("rtp.cooldown");
        COOLDOWN_ERROR = plugin.getConfig().getInt("rtp.cooldown-error");
        MIN_X = plugin.getConfig().getInt("rtp.minx");
        MAX_X = plugin.getConfig().getInt("rtp.maxx");
        MIN_Z = plugin.getConfig().getInt("rtp.minz");
        MAX_Z = plugin.getConfig().getInt("rtp.maxz");
        RTP_WAND_NAME = plugin.getConfig().getString("rtp.rtp_wand");
        if (MIN_Y <= -64 || MIN_Y >= 319){
		    plugin.getConfig().set("rtp.miny", 64);
		    MIN_Y = 64;
	    }
	    if (MAX_Y <= -64 || MAX_Y >= 319){
		    plugin.getConfig().set("rtp.maxy", 100);
		    MAX_Y = 100;
	    }
	    plugin.getConfig().options().copyDefaults(true);
	    plugin.saveConfig();
	    
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    	    Player player = event.getPlayer();
    	    ItemStack item = player.getItemInHand();
    	    CustomStack customStack = CustomStack.byItemStack(item);
            if (customStack != null && customStack.getNamespacedID().equals(RTP_WAND_NAME)) {
		event.setCancelled(true);
                UUID playerId = player.getUniqueId();
                long Time = System.currentTimeMillis() / 1000;

                if (cooldowns.containsKey(playerId)) {
                    long lastUsed = cooldowns.get(playerId);
                    long timeSinceLastUse = Time - lastUsed;

                    if (timeSinceLastUse < COOLDOWN_TIME) {
                        long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                        player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                        return;
                    }
                }
                World world = player.getWorld();
                int x = (int) (Math.random() * (MAX_X - MIN_X) + MIN_X);
                int z = (int) (Math.random() * (MAX_Z - MIN_Z) + MIN_Z);
                int y = 0;
                Location location = new Location(world, x, y, z);
                if (!world.getBiome(location).equals(Biome.RIVER) || !world.getBiome(location).toString().contains("OCEAN")) {
                    y = world.getHighestBlockAt(x,z).getY();
                    Location belowLocation = new Location(world, x, y - 1, z);
                    if (location.getBlock().getType().isAir() && belowLocation.getBlock().getType().isSolid()) {
                        player.teleport(location);
                        player.sendTitle(" §aRTP réussi", "x: " + x + " y: " + y + " z: " + z + " "+(System.currentTimeMillis() / 1000 -Time)+"s");
                        cooldowns.put(playerId, Time);
                        return;
                    }
                    else {
                        player.sendTitle(" §cErreur","/rtp");
                        cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
                        return;
                    }
                }
                player.sendTitle(" §cErreur","/rtp");
                cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
            }
        }
    }
}
