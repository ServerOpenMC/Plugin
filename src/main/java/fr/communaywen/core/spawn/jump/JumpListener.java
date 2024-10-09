package fr.communaywen.core.spawn.jump;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class JumpListener implements Listener {
    private JavaPlugin plugin;
    private FileConfiguration config;

    private final long jumpCooldownRewards = 86400000; // = 1 jour
    private final HashMap<UUID, Long>jumpRewardsCooldown = new HashMap<>();
    private BukkitRunnable eventRunnable;

    private final JumpManager jumpManager;

    public JumpListener(AywenCraftPlugin plugins, JumpManager manager) {
        config = plugins.getConfig();
        plugin = plugins;
        this.jumpManager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (RegionsManager.isPlayerInRegion("spawn", Bukkit.getWorld(config.getString("jump.world")))) {
            if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                Location blockLocation = event.getClickedBlock().getLocation();

                Location startLocation = new Location(Bukkit.getWorld(config.getString("jump.world")), config.getDouble("jump.start.posX"), config.getDouble("jump.start.posY"), config.getDouble("jump.start.posZ"));
                Location endLocation = new Location(Bukkit.getWorld(config.getString("jump.world")), config.getDouble("jump.end.posX"), config.getDouble("jump.end.posY"), config.getDouble("jump.end.posZ"));

                if (blockLocation.equals(startLocation) && !jumpManager.isJumping(event.getPlayer())) {
                    jumpManager.startJump(player);
                    MessageManager.sendMessageType(player, "§aVous avez commencé le jump", Prefix.JUMP, MessageType.SUCCESS, true);

                    eventRunnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (jumpManager.isJumping(player)) {
                                jumpManager.updateActionBar(player, jumpManager.getElapsedSeconds(player));
                            }
                        }
                    };
                    eventRunnable.runTaskTimer(plugin, 0, 1);
                }

                if (blockLocation.equals(endLocation) && jumpManager.isJumping(event.getPlayer())) {
                    double jumpTime = jumpManager.endJump(player);
                    MessageManager.sendMessageType(player, "§aBravo ! Vous avez terminé le jump en §e" + jumpTime + " secondes!", Prefix.JUMP, MessageType.SUCCESS, true);


                    if (System.currentTimeMillis() - jumpRewardsCooldown.getOrDefault(player.getUniqueId(), 0L) > jumpCooldownRewards) {
                        ItemStack luckyblock = LBUtils.getLuckyBlockItem();
                        luckyblock.setAmount(4);
                        MessageManager.sendMessageType(player, "§aVous avez collécté §64 LuckyBlock§a et §61000$§a", Prefix.JUMP, MessageType.SUCCESS, true);

                        player.getInventory().addItem(luckyblock);
                        EconomyManager.addBalanceOffline(player, 1000);
                    }

                    jumpRewardsCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    //BEST RECORD
                }
            }
        }
    }
}
