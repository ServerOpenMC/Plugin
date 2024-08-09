package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Random;

public class EatListener implements Listener {

    AywenCraftPlugin plugin;
    World dreamworld;

    public EatListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        this.dreamworld = plugin.getServer().getWorld("dreamworld");
    }

    @EventHandler
    public void onFoodEated(PlayerItemConsumeEvent event) {
        CustomStack stack = CustomStack.byItemStack(event.getItem());

        if (stack != null) {
            if (!stack.getNamespacedID().equals("aywen:somnifere")) { return; }

            Random r = new Random();
            int range = 1000;
            int x = r.nextInt(range - (range * -1)) + (range * -1);
            int y = r.nextInt(range - (range * -1)) + (range * -1);
            event.getPlayer().teleport(dreamworld.getHighestBlockAt(x, y).getLocation().add(0,1,0));
        }
    }
}
