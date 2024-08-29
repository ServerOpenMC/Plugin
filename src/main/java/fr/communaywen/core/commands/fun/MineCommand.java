package fr.communaywen.core.commands.fun;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.util.Random;

public class MineCommand {

    @Command("mine")
    @Description("Téléporte vers la mine")
    public void tpMine(Player player) {
        FileConfiguration config = AywenCraftPlugin.getInstance().getConfig();
        MultiverseWorld mineWorld = AywenCraftPlugin.getInstance().mvCore.getMVWorldManager().getMVWorld(config.getString("mine.name"));

        if(mineWorld != null || AywenCraftPlugin.getInstance().mvCore != null) {
            assert mineWorld != null;

            Random rand = new Random();

            Location spawn = mineWorld.getSpawnLocation();
            int x = rand.nextInt(3000);
            int z = rand.nextInt(3000);
            int y = mineWorld.getCBWorld().getHighestBlockYAt(x, z);

            Location loc = new Location(mineWorld.getCBWorld(), x, y, z);

            player.teleport(loc);
            player.sendMessage("§aVous venez de vous téléporter à la mine.");
        } else {
            player.sendMessage("§cLe monde dans lequel vous voulez vous téléporter n'existe pas ou plus.");
        }

    }

}
