package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Random;

public class EatListener implements Listener {

    World dreamworld;
    AywenCraftPlugin plugin;

    public EatListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        dreamworld = Bukkit.getWorld("dreamworld");
    }

    @EventHandler
    public void onFoodEated(PlayerItemConsumeEvent event) {
        if (!event.getItem().getType().toString().equals("GLOW_BERRIES")) { return; } // TODO: Somnifères

        Random r = new Random();
        int range = 1000;
        int x = r.nextInt(range-(range*-1)) + (range*-1);
        int y = r.nextInt(range-(range*-1)) + (range*-1);

        Player player = event.getPlayer();
        player.teleport(dreamworld.getHighestBlockAt(x,y).getLocation());
    }
}
