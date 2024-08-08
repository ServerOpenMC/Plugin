package fr.communaywen.core.quests;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.quests.qenum.QUESTS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;


public class QuestsListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        QuestsManager.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event){
        QuestsManager.manageQuestsPlayer(event.getEnchanter(), QUESTS.ENCHANT_FIRST_ITEM, 1, "Objet enchanté");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws SQLException {
        PlayerQuests pq = QuestsManager.getPlayerQuests(event.getPlayer());
        for(QUESTS quests : QUESTS.values())
            QuestsManager.savePlayerQuestProgress(event.getPlayer(), quests, pq.getProgress(quests));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getBlock().getType().equals(Material.DIAMOND_ORE) || event.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE))) {
            QuestsManager.manageQuestsPlayer(event.getPlayer(), QUESTS.BREAK_DIAMOND, 1, "diamants(s) miné(s)");
        } else if ((event.getBlock().getType().equals(Material.STONE) || event.getBlock().getType().equals(Material.DEEPSLATE))) {
            QuestsManager.manageQuestsPlayer(event.getPlayer(), QUESTS.BREAK_STONE, 1, "stone(s) cassé(s)");
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        QuestsManager.manageQuestsPlayer(event.getPlayer(), QUESTS.PLACE_BLOCK, 1, "block placés.");
    }

//    @EventHandler
//    public void onItemPickup(EntityPickupItemEvent event){
//        if(event.getItem().getType().equals(Material.TOTEM_OF_UNDYING)){
//            QuestsManager.manageQuestsPlayer(event., QUESTS.PLACE_BLOCK, 1, "block placés.");
//        }
//    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if(killer == null) return;
        if(killer instanceof Player) {
            QuestsManager.manageQuestsPlayer(killer, QUESTS.KILL_PLAYERS, 1, "joueur(s) tué(s)");
        }
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player == null) return;
        if (event.getEntity().getType().equals(EntityType.WARDEN)) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.KILL_WARDENS, 1, "warden tué");
        } else if (event.getEntity() instanceof Creeper){
            Creeper creeper = (Creeper) event.getEntity();
            if(creeper.isPowered()){
                QuestsManager.manageQuestsPlayer(player, QUESTS.KILL_SUPER_CREEPER, 1, "Creeper super chargé tué");
            }
        }else if (event.getEntity().getType().equals(EntityType.ZOMBIE)){
            QuestsManager.manageQuestsPlayer(player, QUESTS.KILL_ZOMBIE, 1, "zombie tué.");
        }
    }

     @EventHandler
     public void onPlayerMove(PlayerMoveEvent event) {
         Player player = event.getPlayer();
         Location from = event.getFrom();
         Location to = event.getTo();
         assert to != null;

         if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()) {
             QuestsManager.manageQuestsPlayer(player, QUESTS.WALK_BLOCKS, 1, "Block(s) marché(s)");
         }
     }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (getItemsName(event.getRecipe().getResult()).equals("wand:rtpwand")) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.CRAFT_RTP_WAND, 1, "RTP WAND crafté");
        } else if (getItemsName(event.getRecipe().getResult()).equals("aywen:kebab")) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.CRAFT_KEBAB, 1, "kebab crafté");
        } else if (getItemsName(event.getRecipe().getResult()).equals("minecraft:cake")) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.CRAFT_CAKE, 1, "gateau crafté.");
        }

    }

    // on player consume aywen:kebab
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (getItemsName(event.getItem()).equals("aywen:kebab")) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.EAT_KEBAB, 1, "kebab mangé");
        }
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        ItemStack extractedItem = new ItemStack(event.getItemType());
        ItemStack iron = new ItemStack(Material.IRON_INGOT);

        if (extractedItem.equals(iron)) {
            QuestsManager.manageQuestsPlayer(player, QUESTS.SMELT_IRON, event.getItemAmount(), "fer(s) cuit(s)");
        }
    }

    private String getItemsName(ItemStack item) {
        String name;
        CustomStack customstack = CustomStack.byItemStack(item);

        try {
            return name = customstack.getNamespacedID();
        } catch (Exception e) {
            return name = item.getType().name().toLowerCase();
        }
    }

}
