package fr.communaywen.core.dreamdim.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class MilkListener implements Listener {
    HashMap<Player, Date> lastMilk = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e) {
        Player milker = e.getPlayer();
        Entity entity = e.getRightClicked();
        if (!(entity instanceof Player cow)) { return; }

        if (!milker.getWorld().getName().equals("dreamworld")) { return; }
        PlayerInventory inv = milker.getInventory();
        if (inv.getItemInMainHand().getType().equals(Material.BUCKET)) {
            if (lastMilk.containsKey(cow)){
                if ((Date.from(Instant.now()).getTime()-lastMilk.get(cow).getTime()) <= 1000*60*5){
                    return;
                }
            }
            lastMilk.put(cow, Date.from(Instant.now()));

            ItemStack bucket = new ItemStack(Material.MILK_BUCKET);
            bucket.setDisplayName("§rLait de "+cow.getName());
            cow.sendMessage("Vous avez été trait par §l"+milker.getName()+"§r :sweat_drops:");
            milker.sendMessage("vous pourrez traire "+cow.getName()+" dans 5 minutes");

            inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount()-1);
            inv.addItem(bucket);
        }
    }
}
