package fr.communaywen.core.spawn.jump;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.guideline.GuidelineManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import fr.communaywen.core.managers.RegionsManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.getHoverEvent;

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

                if (blockLocation.equals(startLocation) && !JumpManager.isJumping(event.getPlayer())) {
                    jumpManager.startJump(player);
                    MessageManager.sendMessageType(player, "§aVous avez commencé le jump", Prefix.JUMP, MessageType.SUCCESS, true);
                    Component message = Component.text("Pour finir le jump,", NamedTextColor.GREEN)
                            .append(Component.text(" Cliquez-ici", NamedTextColor.YELLOW))
                            .clickEvent(ClickEvent.runCommand("/jump end"))
                            .hoverEvent(getHoverEvent("Arreter le Jump"));
                    event.getPlayer().sendMessage(message);

                    eventRunnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (jumpManager.isJumping(player)) {
                                jumpManager.updateActionBar(player, jumpManager.getElapsedSeconds(player));
                                if (!RegionsManager.isSpecifiedPlayerInRegion(player, "spawn")) {
                                    jumpManager.endJump(player);
                                    MessageManager.sendMessageType(player, "§7Le Jump s'est §carreté §7car vous avez quitter le Spawn", Prefix.JUMP, MessageType.ERROR, true);
                                }
                                if (player.isFlying()) {
                                    jumpManager.endJump(player);
                                    MessageManager.sendMessageType(player, "§7Le Jump s'est §carreté §7car vous avez voler durant le Jump", Prefix.JUMP, MessageType.ERROR, true);
                                }
                                if (player.isGliding()) {
                                    jumpManager.endJump(player);
                                    MessageManager.sendMessageType(player, "§7Le Jump s'est §carreté §7car vous avez voler avec des élytras durant le Jump", Prefix.JUMP, MessageType.ERROR, true);
                                }
                                if (player.isRiptiding()) {
                                    jumpManager.endJump(player);
                                    MessageManager.sendMessageType(player, "§7Le Jump s'est §carreté §7car vous avez voler avec un trident durant le Jump", Prefix.JUMP, MessageType.ERROR, true);
                                }

                                if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                                    jumpManager.endJump(player);
                                    MessageManager.sendMessageType(player, "§7Le Jump s'est §carreté §7car vous avez un Effet de Lévitation durant le Jump\n§7TIPS : Buvez du lait de Courgette", Prefix.JUMP, MessageType.ERROR, true);
                                }
                            }
                        }
                    };
                    eventRunnable.runTaskTimer(plugin, 0, 1);
                }

                if (blockLocation.equals(endLocation) && jumpManager.isJumping(event.getPlayer())) {
                    double jumpTime = jumpManager.endJump(player);
                    MessageManager.sendMessageType(player, "§aBravo ! Vous avez terminé le jump en §e" + jumpTime + " secondes!", Prefix.JUMP, MessageType.SUCCESS, true);

                    double bestTime = jumpManager.getBestTime(player);
                    if (bestTime == -1 || jumpTime < bestTime) {
                        jumpManager.setBestTime(player, jumpTime);
                        MessageManager.sendMessageType(player, "§6§lBEST RECORD! §aNouveau record personnel avec §e" + jumpTime + " secondes!", Prefix.JUMP, MessageType.SUCCESS, true);
                    }

                    if (System.currentTimeMillis() - jumpRewardsCooldown.getOrDefault(player.getUniqueId(), 0L) > jumpCooldownRewards) {
                        ItemStack luckyblock = LBUtils.getLuckyBlockItem();
                        luckyblock.setAmount(4);
                        MessageManager.sendMessageType(player, "§aVous avez collécté §64 LuckyBlock§a et §61000$§a", Prefix.JUMP, MessageType.SUCCESS, true);

                        player.getInventory().addItem(luckyblock);
                        EconomyManager.addBalanceOffline(player, 1000);
                    }

                    GuidelineManager.getAPI().getAdvancement("openmc:spawn/jump/firstjump").grant(event.getPlayer());

                    if (jumpManager.isPlayerInTop10(player)) {
                        GuidelineManager.getAPI().getAdvancement("openmc:spawn/jump/top10jump").grant(event.getPlayer());
                    }

                    jumpRewardsCooldown.put(player.getUniqueId(), System.currentTimeMillis());

                    Location spawn = new Location(player.getServer().getWorld(plugin.getConfig().getString("spawn.world")), plugin.getConfig().getDouble("spawn.x"), plugin.getConfig().getDouble("spawn.y"), plugin.getConfig().getDouble("spawn.z"), 0, 0);
                    player.teleport(spawn);
                }
            }
        }
    }
}
