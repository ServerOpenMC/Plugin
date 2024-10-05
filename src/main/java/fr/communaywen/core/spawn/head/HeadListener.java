package fr.communaywen.core.spawn.head;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fr.communaywen.core.contest.FirerocketSpawnListener.isPlayerInRegion;

public class HeadListener implements Listener {
    static JavaPlugin plugin;
    static FileConfiguration config;

    public HeadListener(AywenCraftPlugin plugins) {
        config = plugins.getConfig();
        plugin = plugins;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
        if (isPlayerInRegion(config.getString("head.region"), Bukkit.getWorld(config.getString("head.world")))) {
            if(event.getClickedBlock().getType() == Material.PLAYER_HEAD || event.getClickedBlock().getType() == Material.PLAYER_WALL_HEAD) {
                    Location clickedBlockLocation = event.getClickedBlock().getLocation();

                    List<Map<?, ?>> headList = config.getMapList("head.list");
                    for (Map<?, ?> headData : headList) {
                        int headId = (int) headData.get("id");
                        double posX = (double) headData.get("posX");
                        double posY = (double) headData.get("posY");
                        double posZ = (double) headData.get("posZ");

                        Location headLocation = new Location(clickedBlockLocation.getWorld(), posX, posY, posZ);
                        if (headLocation.equals(clickedBlockLocation)) {

                            if (!HeadManager.hasFoundHead(event.getPlayer(), String.valueOf(headId))) {
                                HeadManager.initPlayerDataCache(event.getPlayer());
                                HeadManager.saveFoundHead(event.getPlayer(), String.valueOf(headId));
                                MessageManager.sendMessageType(event.getPlayer(), "§7Vous avez trouvé une tête! (§d" + HeadManager.getNumberHeads(event.getPlayer()) + "§8/§d" + HeadManager.getMaxHeads() + "§7)", Prefix.HEAD, MessageType.SUCCESS, true);
                            } else {
                                MessageManager.sendMessageType(event.getPlayer(), "§cVous avez déjà collecté cette tête", Prefix.HEAD, MessageType.ERROR, true);
                            }

                            break;
                        }
                    }
                }
            }
        }
    }
}
