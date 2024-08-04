package fr.communaywen.core.dreamdim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;

import fr.communaywen.core.dreamdim.populators.*;
import fr.communaywen.core.dreamdim.listeners.*;

@Feature("Dream Dimension")
@Credit("Gyro3630")
public class DimensionManager implements Listener {

    AywenCraftPlugin plugin;
    Server server;

    @Getter
    Utils utils;

    public DimensionManager(AywenCraftPlugin Plugin) {
        this.plugin = Plugin;
        this.server = plugin.getServer();
    }

    public void init() {
        createDimension();
        this.utils = new Utils(plugin);
        plugin.registerEvents(
                new EatListener(plugin),
                new EnterWorldListener(plugin)
        );
    }

    public void createDimension() {
        WorldCreator creator = new WorldCreator("dreamworld");
        creator.generator(new DreamChunkGenerator());
        creator.environment(World.Environment.NORMAL);
        World dreamworld = creator.createWorld();

        if (dreamworld != null) {
            dreamworld.getPopulators().add(new LightPopulator());
            dreamworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            dreamworld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            dreamworld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            dreamworld.setGameRule(GameRule.DISABLE_RAIDS, true);
            dreamworld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            dreamworld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);

            dreamworld.setTime(18000);

            plugin.getLogger().info("Dream World created successfully!");
        } else {
            plugin.getLogger().warning("Failed to create sculk dimension!");
        }
    }
}
