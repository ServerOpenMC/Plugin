package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class KebabListener implements Listener {

    private final AywenCraftPlugin plugin;

    public KebabListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private void addSaturation(Player player, int amount){
        player.setSaturation(player.getSaturation() + amount);
    }

    private void addFood(Player player, int amount){
        player.setFoodLevel(player.getFoodLevel() + amount);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        CustomStack customstack = CustomStack.byItemStack(item);

        if (customstack == null){ return; }

        if (customstack.getNamespacedID().equals("aywen:kebab")) {
            addFood(player, 4);
            addSaturation(player, 4);
        } else if (customstack.getNamespacedID().equals("aywen:onion")) {
            addFood(player, 1);
            addSaturation(player, 1);
        } else if (customstack.getNamespacedID().equals("aywen:salade")) {
            addFood(player, 1);
            addSaturation(player, 1);
        } else if (customstack.getNamespacedID().equals("aywen:tomate")) {
            addFood(player, 1);
            addSaturation(player, 1);
        } else if(customstack.getNamespacedID().equals("aywen:frite")) {
            addFood(player, 2);
            addSaturation(player, 2);
        }
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (!damager.getType().equals(EntityType.PLAYER)){ return;}
        if (!damaged.getType().equals(EntityType.PLAYER)){ return;}
        Player player = (Player) damager;
        Player damagedPlayer = (Player) damaged;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!player.isOp()){
            return;
        }

        CustomStack customstack = CustomStack.byItemStack(item);
        if (customstack == null){ return; }

        if (customstack.getNamespacedID().equals("aywen:spatule")) {
            this.plugin.getServer().broadcastMessage("Au lit "+damaged.getName()+"!");
            damagedPlayer.damage(100);
        } else {
            player.sendMessage(customstack.getNamespacedID());
        }
    }
}
