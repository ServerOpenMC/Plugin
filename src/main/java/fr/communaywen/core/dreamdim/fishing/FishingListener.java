package fr.communaywen.core.dreamdim.fishing;

import fr.communaywen.core.dreamdim.fishing.loot_table.Junk;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FishingListener implements Listener {
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!(player.getWorld().getName().equals("dreamworld"))) { return; }

        if (!(event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))) { return; }

        double categoryChance = new Random().nextDouble();
        LootCategory category = null;
        
        if (categoryChance <= 1/1) {
            category = new Junk();
        }

        ItemStack reward = category.pickOne(0).toItemStack(player);

        if (event.getCaught() == null) { return; }

        if (event.getCaught() instanceof Item item) {
            item.setItemStack(reward);
        }
    }
}
