package fr.communaywen.core.listeners;

import fr.communaywen.core.credit.Credit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
@Credit("fuzeblocks")
public class FallBloodListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                double degats = player.getFallDistance() / 1.5;
                player.damage(degats);
                player.addPotionEffect(getPotionEffectType());
                player.sendMessage("§l§cVous saignez !");
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(90, 7, 25), 1.0F);
                player.spawnParticle(Particle.EXPLOSION, player.getLocation(), 9999, dustOptions);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType().equals(Material.PAPER) && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (Objects.requireNonNull(meta).hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equals("Bandage")) {
                event.setCancelled(true);

                if (player.hasPotionEffect(PotionEffectType.POISON)) {
                        player.removePotionEffect(PotionEffectType.POISON);
                    int amount = item.getAmount();
                    if (amount > 1) {
                        item.setAmount(amount - 1);
                    } else {
                        player.getInventory().remove(item);
                    }
                }
            }
        }
    }
    private PotionEffect getPotionEffectType() {
       return new PotionEffect(PotionEffectType.POISON, 110, 1);
    }
}