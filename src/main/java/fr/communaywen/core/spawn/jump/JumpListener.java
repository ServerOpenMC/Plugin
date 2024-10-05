package fr.communaywen.core.spawn.jump;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.FirerocketSpawnListener;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JumpListener implements Listener {
    static JavaPlugin plugin;
    static FileConfiguration config;

    public JumpListener(AywenCraftPlugin plugins) {
        config = plugins.getConfig();
        plugin = plugins;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (FirerocketSpawnListener.isPlayerInRegion("spawn", Bukkit.getWorld(config.getString("jump.world")))) {
            if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                Location blockLocation = event.getClickedBlock().getLocation();

                Location startLocation = new Location(Bukkit.getWorld(config.getString("jump.world")), config.getDouble("jump.start.posX"), config.getDouble("jump.start.posY"), config.getDouble("jump.start.posZ"));
                Location endLocation = new Location(Bukkit.getWorld(config.getString("jump.world")), config.getDouble("jump.end.posX"), config.getDouble("jump.end.posY"), config.getDouble("jump.end.posZ"));

                if (blockLocation.equals(startLocation) && !JumpManager.isJumping(event.getPlayer())) {
                    JumpManager.startJump(player);
                    MessageManager.sendMessageType(player, "§aVous avez commencé le jump", Prefix.JUMP, MessageType.SUCCESS, true);
                }

                if (blockLocation.equals(endLocation) && JumpManager.isJumping(event.getPlayer())) {
                    double jumpTime = JumpManager.endJump(player);
                    MessageManager.sendMessageType(player, "§aBravo ! Vous avez terminé le jump en §e" + jumpTime + " secondes!", Prefix.JUMP, MessageType.SUCCESS, true);

                    //BEST RECORD
                }
            }
        }
    }
}
