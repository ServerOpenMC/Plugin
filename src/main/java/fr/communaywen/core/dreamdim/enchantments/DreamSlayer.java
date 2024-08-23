package fr.communaywen.core.dreamdim.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DreamSlayer implements Listener {
    Enchantment dreamSlayer;

    public DreamSlayer() {
        this.dreamSlayer = Registry.ENCHANTMENT.get(NamespacedKey.fromString("aywen:dream_slayer"));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) { return; }
        if (!player.getWorld().getName().equals("dreamworld")) { return; }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.hasEnchant(dreamSlayer)) {
            int level = item.getEnchantLevel(dreamSlayer);
            event.setDamage(event.getDamage() + (0.5*(level-1))+1); //https://fr.minecraft.wiki/w/Tranchant
        }
    }
}
