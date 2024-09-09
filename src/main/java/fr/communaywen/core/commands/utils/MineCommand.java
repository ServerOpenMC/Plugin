package fr.communaywen.core.commands.utils;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.MineUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;

import java.util.Random;

public class MineCommand {

    @Command("mine")
    @Description("Téléporte vers la mine")
    public void onCommand(Player player, @Named("Biome") @Optional String biome) {
        MultiverseWorld mineWorld = AywenCraftPlugin.getInstance().getMineWorld();
        
        if(mineWorld != null || AywenCraftPlugin.getInstance().mvCore != null) {
            assert mineWorld != null;
            
            if (biome != null) {
                Location loc = MineUtils.getLocationOfBiome(biome);
                if (loc != null) player.teleport(loc);
            } else {
                Random rand = new Random();
                
                Location spawn = mineWorld.getSpawnLocation();
                int x = rand.nextInt(3000);
                int z = rand.nextInt(3000);
                int y = mineWorld.getCBWorld().getHighestBlockYAt(x, z);
                
                Location loc = new Location(mineWorld.getCBWorld(), x, y, z);
                
                player.teleport(loc);
            }
            player.sendMessage("§aVous venez de vous téléporter à la mine.");
        } else {
            player.sendMessage("§cLe monde dans lequel vous voulez vous téléporter n'existe pas ou plus.");
        }

    }

}
