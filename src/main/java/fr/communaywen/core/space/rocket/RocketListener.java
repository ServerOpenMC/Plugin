package fr.communaywen.core.space.rocket;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.menu.TeamMenu;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RocketListener implements Listener {

    @EventHandler
    public void onRocketInteract(PlayerInteractEntityEvent event) {
        if(event.isCancelled()) return;
        if(CustomEntity.isCustomEntity(event.getRightClicked())) {
            CustomEntity rocket = CustomEntity.byAlreadySpawned(event.getRightClicked());
            if(!rocket.getNamespacedID().equals("space:rocket")) return;
            if(rocket != null) {

                if(Rocket.getByEntity(rocket) == null) {
                    new Rocket(rocket);
                }

                Rocket.getByEntity(rocket).openMenu(event.getPlayer());


            }
        }
    }

    @EventHandler
    public void onRocketPlace(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.isCancelled()) return;

        ItemStack item = event.getItem();
        CustomStack customStack = CustomStack.byItemStack(item);
        if(customStack != null && customStack.getNamespacedID().equals("space:rocket")) {
            if(event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR && event.getClickedBlock().getType().isSolid()) {
                if(event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR && event.getClickedBlock().getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR){
                    CustomEntity rocket = CustomEntity.spawn("space:rocket", event.getClickedBlock().getLocation().add(0.5, 1.25, 0.5));
                    rocket.playAnimation("animation.rocket.idle");
                    new Rocket(rocket);
                    event.getPlayer().getInventory().removeItem(item);
                }
            }
        }
    }


    @EventHandler
    public void onRocketBreak(CustomEntityDeathEvent event) {
        if(event.getNamespacedID().equals("space:rocket")) {
            if(CustomEntity.byAlreadySpawned(event.getEntity()) == null) AywenCraftPlugin.getInstance().getLogger().info("Unknown rocket");
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), CustomStack.getInstance("space:rocket").getItemStack());
            Rocket rocket = Rocket.getByEntity(CustomEntity.byAlreadySpawned(event.getEntity()));
            if(rocket != null) {
                rocket.getGui().getInventory().close();
                rocket.remove();
                if(rocket.getCoalCount() > 0) {
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.COAL, rocket.getCoalCount()));
                }
            }
        }
    }

    @EventHandler
    public void onSpecTP(PlayerTeleportEvent event) {
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            if(!event.getPlayer().hasPermission("minecraft.command.teleport") && !event.getPlayer().hasPermission("essentials.teleport")) {
                event.setCancelled(true);
            }
        }
    }


}
