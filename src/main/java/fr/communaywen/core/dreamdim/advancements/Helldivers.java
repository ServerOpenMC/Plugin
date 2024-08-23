package fr.communaywen.core.dreamdim.advancements;

import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Helldivers implements Listener {
    AdvancementRegister register;

    public Helldivers(AdvancementRegister register) {
        /* Accordée quand le joueur tue un mob de chaque type dans la dimension de rêves */
        this.register = register;
    }

    private void increment(UUID player, String key, Integer amount) {
        HashMap<String, Integer> pAdvancements = register.advancements.get(player);
        pAdvancements.put(key, pAdvancements.getOrDefault(key, 0)+amount);
    }

    private void grantCriteria(Player player, String criteria) {        
        UUID uuid = player.getUniqueId();
        
        HashMap<String, Integer> pAdvancements = register.advancements.get(uuid);
        
        if (pAdvancements.get(criteria) >= 10000) {
            register.grantAdvancement(player, "aywen:helldivers/hd6", criteria);
        } else if (pAdvancements.get(criteria) >= 1000) {
            register.grantAdvancement(player, "aywen:helldivers/hd5", criteria);
        } else if (pAdvancements.get(criteria) >= 500) {
            register.grantAdvancement(player, "aywen:helldivers/hd4", criteria);
        } else if (pAdvancements.get(criteria) >= 100) {
            register.grantAdvancement(player, "aywen:helldivers/hd3", criteria);
        } else if (pAdvancements.get(criteria) >= 10) {
            register.grantAdvancement(player, "aywen:helldivers/hd2", criteria);
        } else if (pAdvancements.get(criteria) >= 1) {
            register.grantAdvancement(player, "aywen:helldivers/hd1", criteria);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (!(entity.getKiller() instanceof Player player)) { return; }
        if (!player.getWorld().getName().equals("dreamworld")) { return; }
        if (!entity.getWorld().getName().equals("dreamworld")) { return; }
        UUID playeruuid = player.getUniqueId();

        if (entity.getType().equals(EntityType.SPIDER)) {
            increment(playeruuid, "spider", 1);
            grantCriteria(player, "spider");
        } else if (entity.getType().equals(EntityType.ZOMBIE_HORSE)) {
            increment(playeruuid, "zombie_horse", 1);
            grantCriteria(player, "zombie_horse");
        } else if (entity.getType().equals(EntityType.SKELETON_HORSE)) {
            increment(playeruuid, "skeleton_horse", 1);
            grantCriteria(player, "skeleton_horse");
        }
    }
}
