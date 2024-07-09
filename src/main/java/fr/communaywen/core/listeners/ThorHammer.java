package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.FallingBlocksExplosion;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ThorHammer implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
    	    Player player = event.getPlayer();

            if(!player.isSneaking()) return;

            useThorHammer(player, event.getClickedBlock().getLocation());
        }
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() == EntityType.PLAYER ) {
            Player player = (Player) event.getDamager();

            Location loc = new Location(event.getEntity().getWorld(), event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockY(), event.getEntity().getLocation().getBlockZ());

            useThorHammer(player, loc);

        }
    }

    @SuppressWarnings("deprecation")
    private void useThorHammer(Player player, Location loc) {

        ItemStack item = player.getItemInHand();
        CustomStack customStack = CustomStack.byItemStack(item);

        if(customStack != null && customStack.getNamespacedID().equals("thor:hammer")) {
            if(player.getCooldown(item.getType()) > 0) return;
            if(player.getGameMode() != GameMode.CREATIVE) item.setDurability((short) (item.getDurability() + 1));
            if(item.getDurability() >= item.getType().getMaxDurability()) {
                player.getInventory().remove(item);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                return;
            }
            if(player.getGameMode() != GameMode.CREATIVE) player.setCooldown(item.getType(), 1200);
            World world = loc.getWorld();
            assert world != null;
            world.strikeLightning(loc);
            new FallingBlocksExplosion(3, loc, false);
            world.playSound(loc, Sound.ITEM_TOTEM_USE, 1, 0);
            world.spawnParticle(Particle.CLOUD, loc, 3, 0.02d, 1d, 0.02d, 0.09d);
        }
    }
}
