package fr.communaywen.core.spawn.head;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import fr.communaywen.core.managers.RegionsManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class HeadListener implements Listener {
    static JavaPlugin plugin;
    static FileConfiguration config;

    public HeadListener(AywenCraftPlugin plugins) {
        config = plugins.getConfig();
        plugin = plugins;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
        if (RegionsManager.isPlayerInRegion(config.getString("head.region"), Bukkit.getWorld(config.getString("head.world")))) {
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
                            if (!HeadManager.hasFoundHead(player, String.valueOf(headId))) {
                                HeadManager.saveFoundHead(player, String.valueOf(headId));
                                HeadManager.initPlayerDataCache(player);

                                MessageManager.sendMessageType(player, "§7Vous avez trouvé une tête! (§d" + HeadManager.getNumberHeads(player) + "§8/§d" + HeadManager.getMaxHeads() + "§7)", Prefix.HEAD, MessageType.SUCCESS, true);

                                MessageManager.sendMessageType(player, "§aVous avez collecté §62 LuckyBlock", Prefix.HEAD, MessageType.SUCCESS, true);

                                ItemStack luckyblock = LBUtils.getLuckyBlockItem();
                                luckyblock.setAmount(2);
                                player.getInventory().addItem(luckyblock);

                                GuidelineManager.getAPI().getAdvancement("openmc:spawn/head/1").grant(player);

                                if (HeadManager.getNumberHeads(player) == HeadManager.getMaxHeads()) {
                                    GuidelineManager.getAPI().getAdvancement("openmc:spawn/head/all").grant(player);
                                }
                            } else {
                                MessageManager.sendMessageType(player, "§cVous avez déjà collecté cette tête", Prefix.HEAD, MessageType.ERROR, true);
                            }
                        }
                    }
                }
            }
        }
    }
}
