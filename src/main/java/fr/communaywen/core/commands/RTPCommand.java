package fr.communaywen.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class RTPCommand implements CommandExecutor {

	
	

    private final Configs plugin;
    public RTPCommand(Congis plugin){
	    this.plugin = plugin;
    }


    //Merci à ri1ongithub pour le système de cooldown que j'avais la flème de refaire
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN = this.plugin.getConfig().getint("rtp.cooldown"); //temps en secondes
    private static final int COOLDOWN_ERROR = this.plugin.getConfig().getint("rtp.cooldown-error"); //temps en secondes
    
    
    
	
	@Override
    public boolean onCommand(final CommandSender sender,final Command command,final String label,final String[] args) {
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
            int minx = this.plugin.getConfig().getint("rtp.minx");
            int maxx = this.plugin.getConfig().getint("rtp.maxx");
            int miny = this.plugin.getConfig().getint("rtp.miny");
            int maxy = this.plugin.getConfig().getint("rtp.maxy");
            int minz = this.plugin.getConfig().getint("rtp.minz");
            int maxz = this.plugin.getConfig().getint("rtp.maxz");
            int x = (int) ((Math.random() * (maxx - minx)) + minx);
            int z = (int) ((Math.random() * (maxz - minz)) + minz);
            World world = player.getWorld();
            Location location,belowlocation;
            for (int y = miny; y<maxy;y++) {
            	location = new Location(world,x,y,z);
            	if (location.getBlock().getType().isAir()) {
            		belowlocation = new Location(world,x,y-1,z);
            		if (belowlocation.getBlock().getType().isBlock()) {
            			player.teleport(location);
            			player.sendTitle("§aRTP réussi","x: "+x+" y: "+y+" z: "+z);
            			cooldowns.put(playerId, currentTime);
            			return true;
            		}
            	}
            }
            player.sendTitle(" §cErreur",null);
            cooldowns.put(playerId, currentTime-COOLDOWN_TIME+COOLDOWN_ERROR); //5 secondes de cooldown
            return true;
            
        }
        return true;
    }
}
