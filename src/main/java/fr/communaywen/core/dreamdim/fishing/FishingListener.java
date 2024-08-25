package fr.communaywen.core.dreamdim.fishing;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.dreamdim.SimpleAdvancementRegister;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import fr.communaywen.core.dreamdim.fishing.loot_table.*;

public class FishingListener implements Listener {
    AywenCraftPlugin plugin;

    public FishingListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (!(player.getWorld().getName().equals("dreamworld"))) { return; }

        if (!(event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))) { return; }
        SimpleAdvancementRegister.grantAdvancement(player, "aywen:fishing/root");

        /* Picking a random category */
        LootCategory category = null;
        List<LootCategory> categories = List.of(new Fish(), new Junk(), new Treasures());
        int lucklevel = player.getInventory().getItemInMainHand().getEnchantLevel(Enchantment.LUCK_OF_THE_SEA);

        double totalChance = 0.0;
        for (LootCategory cat : categories) {
            totalChance += cat.getChance(lucklevel);
        }
        if (totalChance != 100) {
            throw new IllegalArgumentException("Invalid chances sum for fishing loots ("+totalChance+")");
        }

        // Generate a random number between 0 and totalChance
        Random random = new Random();
        double randomValue = random.nextDouble()*100;

        // Iterate through the categories and select one based on the random value
        double cumulativeChance = 0.0;
        for (LootCategory cat : categories) {
            cumulativeChance += cat.getChance(lucklevel);
            if (randomValue < cumulativeChance) {
                category = cat;
                break;
            }
        }
        /* ----------- */        
        LootStack loot = category.pickOne();
        ItemStack reward = loot.toItemStack(player);

        if (event.getCaught() == null) { return; }

        if (event.getCaught() instanceof Item item) {
            item.setItemStack(reward);
        }
        loot.onCatched(player, event.getHook());
    }
}
