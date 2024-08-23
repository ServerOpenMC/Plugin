package fr.communaywen.core.dreamdim.enchantments;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

public class Coma implements Listener {
    AywenCraftPlugin plugin;
    public Coma(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.fromString("aywen:coma"));

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        Entity hit = event.getHitEntity();

        if (hit instanceof Player) { return; }
        if (!(event.getEntity() instanceof Arrow arrow)) { return; }
        if (!(arrow.getShooter() instanceof Player shooter)) { return; }
        if (!shooter.getInventory().getItemInMainHand().hasEnchant(enchantment)) { return; }

        int level = shooter.getInventory().getItemInMainHand().getEnchantLevel(enchantment);
        double chance = -0.5 * Math.pow(level, 2) + 4 * level - 1;

        if (new Random().nextDouble()*100 > chance) { return; }
        if (!(hit instanceof LivingEntity livingEntity)) { return; }
        livingEntity.setAI(false);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            livingEntity.setAI(true);
        }, 5*20);
    }
}
