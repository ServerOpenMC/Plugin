package fr.communaywen.core.space.moon;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


import java.util.Objects;

@Feature("Moon")
@Credit("Gyro3630")
public class MoonDimensionManager implements Listener {

    AywenCraftPlugin plugin;
    Server server;

    @Getter
    Utils utils;

    public MoonDimensionManager(AywenCraftPlugin Plugin) {
        this.plugin = Plugin;
        this.server = plugin.getServer();
    }

    public void init() {
        createDimension();
        this.utils = new Utils(plugin);
    }

    public void createDimension() {
        WorldCreator creator = new WorldCreator("moon");
        creator.biomeProvider(new MoonBiomeProvider(creator.seed()));
        creator.generator(new MoonChunkGenerator());
        creator.environment(World.Environment.NORMAL);
        World moonworld = creator.createWorld();

        assert moonworld != null;
        moonworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        moonworld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        moonworld.setGameRule(GameRule.DISABLE_RAIDS, true);
        moonworld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        moonworld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);

        moonworld.setTime(18000);

        plugin.getLogger().info("Moon Dimension created successfully!");
    }

    public void close() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getWorld().getName().equals("moon")) {
                player.teleport(Objects.requireNonNullElse(
                        player.getRespawnLocation(),
                        Bukkit.getWorld("world").getSpawnLocation()
                ));
            }
        }
    }
}