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

	
	//Merci à ri1ongithub pour le système de cooldown que j'avais la flème de refaire
	//Je n'ai pas fait de test mais normalement il y a pas de bug (j'ai quand même testé si l'aléatoir marchait)

	
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_TIME = 60; //temps en secondes
    
    
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
            int minx = -3000 ;
            int maxx = 3000 ;
            int minz = -3000 ;
            int maxz = 3000 ;
            int x = (int) ((Math.random() * (maxx - minx)) + minx);
            int z = (int) ((Math.random() * (maxz - minz)) + minz);
            World world = player.getWorld();
            Location location,belowlocation;
            for (int y = 64; y<100;y++) {
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
            cooldowns.put(playerId, currentTime-COOLDOWN_TIME+5); //5 secondes de cooldown
            return true;
            
        }
        return true;
    }
}
