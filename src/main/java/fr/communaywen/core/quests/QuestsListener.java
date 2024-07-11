package fr.communaywen.core.quests;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestsListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        QuestsManager.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        QuestsManager.savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getBlock().getType().equals(Material.DIAMOND_ORE) || event.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE))) {
            QuestsManager.manageQuestsPlayer(event.getPlayer(), QuestsManager.QUESTS.BREAK_DIAMOND, 1, "diamants(s) miné(s)");
        } else if((event.getBlock().getType().equals(Material.STONE) || event.getBlock().getType().equals(Material.DEEPSLATE))) {
            QuestsManager.manageQuestsPlayer(event.getPlayer(), QuestsManager.QUESTS.BREAK_STONE, 1, "stone(s) cassé(s)");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        assert killer != null;
        QuestsManager.manageQuestsPlayer(killer, QuestsManager.QUESTS.KILL_PLAYERS, 1, "joueur(s) tué(s)");
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if(player == null) return;
        if(event.getEntity().getType().equals(EntityType.WARDEN)) {
            QuestsManager.manageQuestsPlayer(player, QuestsManager.QUESTS.KILL_WARDENS, 1, "warden tué");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        assert to != null;

        if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()) {
            QuestsManager.manageQuestsPlayer(player, QuestsManager.QUESTS.WALK_BLOCKS, 1, "Block(s) marché(s)");
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        CustomStack stackRTPWAND = CustomStack.getInstance(AywenCraftPlugin.getInstance().getConfig().getString("rtp.rtp_wand"));
        ItemStack itemStackWAND = stackRTPWAND.getItemStack();

        if(event.getRecipe().getResult().getItemMeta().equals(itemStackWAND.getItemMeta())) {
            QuestsManager.manageQuestsPlayer(player, QuestsManager.QUESTS.CRAFT_RTP_WAND, 1, "RTP WAND crafté");
        } else if(event.getRecipe().getResult().getItemMeta().getDisplayName().equals("§fKebab")) {
            QuestsManager.manageQuestsPlayer(player, QuestsManager.QUESTS.CRAFT_KEBAB, 1, "kebab crafté");
        }
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        ItemStack extractedItem = new ItemStack(event.getItemType());
        ItemStack iron = new ItemStack(Material.IRON_INGOT);

        if(extractedItem.equals(iron)) {
            QuestsManager.manageQuestsPlayer(player, QuestsManager.QUESTS.SMELT_IRON, event.getItemAmount(), "fer(s) cuit(s)");
        }
    }

}
