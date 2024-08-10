package fr.communaywen.core.space.moon;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class Utils {
    World dreamworld;
    AywenCraftPlugin plugin;

    public Utils(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        this.dreamworld = Bukkit.getWorld("moon");
    }

    public void joinDimension(Player player) {
        this.plugin.getLogger().info(player.getName()+" à été transporté dans la dimension des rêves");

        Random r = new Random();
        int range = 1000;
        int x = r.nextInt(range - (range * -1)) + (range * -1);
        int y = r.nextInt(range - (range * -1)) + (range * -1);
        player.teleport(dreamworld.getHighestBlockAt(x, y).getLocation().add(0,1,0));
    }
}
