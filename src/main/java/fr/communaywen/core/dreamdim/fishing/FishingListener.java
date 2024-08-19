package fr.communaywen.core.dreamdim.fishing;

import fr.communaywen.core.dreamdim.fishing.loot_table.Fish;
import fr.communaywen.core.dreamdim.fishing.loot_table.Junk;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FishingListener implements Listener {
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!(player.getWorld().getName().equals("dreamworld"))) { return; }

        if (!(event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))) { return; }

        double categoryChance = new Random().nextDouble();
        LootCategory category = null;


        /* Picking a random category */
        List<LootCategory> categories = new ArrayList<>(List.of(new Fish(), new Junk()));

        categories.sort(Comparator.comparingDouble(LootCategory::getChance));

        double totalChance = 0.0;
        for (LootCategory cat : categories) {
            totalChance += cat.getChance();
        }
        if (totalChance != 1) {
            throw new IllegalArgumentException("Invalid chances sum for fishing loots");
        }

        // Generate a random number between 0 and totalChance
        Random random = new Random();
        double randomValue = random.nextDouble();

        // Iterate through the categories and select one based on the random value
        double cumulativeChance = 0.0;
        for (LootCategory cat : categories) {
            cumulativeChance += cat.getChance();
            if (randomValue < cumulativeChance) {
                category = cat;
                break;
            }
        }
        /* ----------- */

        LootStack loot = category.pickOne(0);
        ItemStack reward = loot.toItemStack(player);

        loot.onCatched(player);
        if (event.getCaught() == null) { return; }

        if (event.getCaught() instanceof Item item) {
            item.setItemStack(reward);
        }
    }
}
