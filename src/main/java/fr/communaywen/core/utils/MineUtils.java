package fr.communaywen.core.utils;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class MineUtils {
	
	public static FileConfiguration config;
	private final AywenCraftPlugin plugin;
	private static MultiverseWorld mineWorld = AywenCraftPlugin.getInstance().getMineWorld();
	
	public MineUtils(AywenCraftPlugin plugin, FileConfiguration config) {
		this.plugin = plugin;
		this.config = config;
	}
	
	public static Location getLocationOfBiome(String biome) {
		if (config.contains("biomes." + biome)) {
			double x = config.getDouble("biomes." + biome + ".location.x");
			double y = config.getDouble("biomes." + biome + ".location.y");
			double z = config.getDouble("biomes." + biome + ".location.z");
			return new Location(mineWorld.getCBWorld(), x, y, z);
		}
		return null;
	}
}
