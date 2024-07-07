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
    private int MAX_TRY;
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
        MAX_TRY = plugin.getConfig().getInt("rtp.max_try");
	    plugin.getConfig().options().copyDefaults(true);
	    plugin.saveConfig();
	    
    }

    @SuppressWarnings("deprecation")
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
                long ExactTime = System.currentTimeMillis();

                if (cooldowns.containsKey(playerId)) {
                    long lastUsed = cooldowns.get(playerId);
                    long timeSinceLastUse = Time - lastUsed;

                    if (timeSinceLastUse < COOLDOWN_TIME) {
                        long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                        return ;
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
                            player.setCooldown(item.getType(),COOLDOWN_TIME*20);
                            return;
                        }
                    }
                    if (i==3){
                        player.sendTitle(" §cErreur","");
                        cooldowns.put(playerId, Time - COOLDOWN_TIME + COOLDOWN_ERROR);
                        player.setCooldown(item.getType(),COOLDOWN_ERROR*20);
                    }
                }
            }
        }
    }
}
