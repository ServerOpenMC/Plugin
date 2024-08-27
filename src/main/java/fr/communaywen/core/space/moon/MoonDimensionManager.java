package fr.communaywen.core.space.moon;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


import java.util.Objects;

@Feature("Moon")
@Credit({"ri1_", "Gyro3630"})
@Collaborators({"gab400", "Auburn", "CoolJWB", "Claude"})
public class MoonDimensionManager implements Listener {

    AywenCraftPlugin plugin;
    Server server;

    public MoonDimensionManager(AywenCraftPlugin Plugin) {
        this.plugin = Plugin;
        this.server = plugin.getServer();
    }

    public void init() {
        createDimension();
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
                player.getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
                player.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
                player.teleport(Objects.requireNonNullElse(
                        player.getRespawnLocation(),
                        Bukkit.getWorld("world").getSpawnLocation()
                ));
            }
        }
    }
}
