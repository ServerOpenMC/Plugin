package fr.communaywen.core.commands.fun;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class MineCommand {
    private final int MIN_X = -3000;
    private final int MAX_X = 3000;
    private final int MIN_Z = -3000;
    private final int MAX_Z = 3000;


    @Command("mine")
    @Description("Téléporte vers la mine")
    public void tpMine(Player player) {

        FileConfiguration config = AywenCraftPlugin.getInstance().getConfig();
        MultiverseWorld mineWorld = AywenCraftPlugin.getInstance().mvCore.getMVWorldManager().getMVWorld(config.getString("mine.name"));

        if(mineWorld != null || AywenCraftPlugin.getInstance().mvCore != null) {
            int x = (int) (Math.random() * (MAX_X - MIN_X) + MIN_X);
            int z = (int) (Math.random() * (MAX_Z - MIN_Z) + MIN_Z);
            World world = player.getWorld();

            if (!world.getBiome(new Location(world, x, 64, z)).equals(Biome.RIVER) || !world.getBiome(new Location(world, x, 64, z)).toString().contains("OCEAN")) {
                int y = world.getHighestBlockAt(new Location(world, x, 64, z)).getY();
                Location location = new Location(world, x, y + 1, z);
                if (new Location(world, x, y, z).getBlock().getType().isSolid()) {
                    player.teleport(location);
                    player.sendMessage(" §aVous venez de vous téléporter à la mine: x: §f" + x + " §ay: §f" + y + " §az: §f" + z);
                }
            }
        } else {
            player.sendMessage("§cLe monde dans lequel vous voulez vous téléporter n'existe pas ou plus.");
        }

    }

}
