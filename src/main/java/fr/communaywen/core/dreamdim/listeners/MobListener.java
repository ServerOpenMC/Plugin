package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobListener implements Listener {

    public boolean cantLive() {
        return !(new Random().nextDouble() <= 0.15);
    }

    public boolean hasReachCap(Entity entity) {
        return Arrays.stream(entity.getLocation().getChunk().getEntities()).findAny().isPresent();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        World world = entity.getWorld();
        if (!entity.getWorld().getName().equals("dreamworld")) { return; }
        if ((entity instanceof Player)) { return; }

        if (List.of(EntityType.CREEPER, EntityType.WITCH, EntityType.ENDERMAN, EntityType.ZOMBIE_VILLAGER).contains(entity.getType())) {
            e.setCancelled(true);
        } else if (List.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER).contains(entity.getType())) {
            if (hasReachCap(entity)) {
                e.setCancelled(true);
                return;
            }

            if (cantLive()) {
                e.setCancelled(true);
                return;
            }

            double choice = new Random().nextDouble();

            if (choice <= (double) 1 /3) {
                world.spawnEntity(entity.getLocation(), EntityType.ZOMBIE_HORSE);
            } else if (choice <= (double) 2 /3) {
                world.spawnEntity(entity.getLocation(), EntityType.SKELETON_HORSE);
            } else {
                Spider spider = (Spider) world.spawnEntity(entity.getLocation(), EntityType.SPIDER);

                spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(32);
                spider.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(2);
                spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        World world = entity.getWorld();
        if (!world.getName().equals("dreamworld")) { return; }
        if ((entity instanceof Player)) { return; }

        if (entity.getType().equals(EntityType.SPIDER)) {
            e.setDroppedExp(e.getDroppedExp() * 2);
            for (ItemStack item : e.getDrops()) {
                world.dropItem(entity.getLocation(), item);
            }
        }
    }
}
