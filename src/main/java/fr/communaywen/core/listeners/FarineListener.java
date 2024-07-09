package fr.communaywen.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;


public class FarineListener implements Listener {

    @EventHandler
    public void onPlayerUseFarine(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().hasItemMeta()) {
            ItemMeta meta = event.getItem().getItemMeta();
            if (meta != null && "Farine".equals(meta.getDisplayName())) {

                Player player = event.getPlayer();

                Random rand = new Random();
                int chance = rand.nextInt(100) + 1;


                if (chance <= 70) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 300, 1));
                } else if (chance <= 90) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 600, 1));
                    player.damage(4.0);
                } else {
                    player.setHealth(0.0);
                }

                event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        }
    }
}
