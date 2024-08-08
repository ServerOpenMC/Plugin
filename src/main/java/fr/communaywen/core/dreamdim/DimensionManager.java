package fr.communaywen.core.dreamdim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.communaywen.core.dreamdim.populators.*;
import fr.communaywen.core.dreamdim.listeners.*;

import java.util.Objects;

@Feature("Dream Dimension")
@Credit("Gyro3630")
public class DimensionManager implements Listener {

    AywenCraftPlugin plugin;
    Server server;
    AdvancementRegister register;

    @Getter
    Utils utils;

    public DimensionManager(AywenCraftPlugin Plugin) {
        this.plugin = Plugin;
        this.server = plugin.getServer();
    }

    public void init() {
        createDimension();
        this.utils = new Utils(plugin);
        this.register = new AdvancementRegister(plugin);
        plugin.registerEvents(
                new EatListener(plugin),
                new EnterWorldListener(plugin, register),
                new DisableSculk(),
                new MobListener()
        );
    }

    public void createDimension() {
        WorldCreator creator = new WorldCreator("dreamworld");
        creator.biomeProvider(new DreamBiomeProvider(creator.seed()));
        creator.generator(new DreamChunkGenerator());
        creator.environment(World.Environment.NORMAL);
        World dreamworld = creator.createWorld();

        assert dreamworld != null;
        dreamworld.getPopulators().add(new LightPopulator());
        dreamworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        dreamworld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        dreamworld.setGameRule(GameRule.DISABLE_RAIDS, true);
        dreamworld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        dreamworld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);

        dreamworld.setTime(18000);

        plugin.getLogger().info("Dream World created successfully!");
    }

    public void close() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            /* Explusion du territoire */
            if (player.getWorld().getName().equals("dreamworld")) {
                player.teleport(Objects.requireNonNullElse(
                        player.getRespawnLocation(),
                        Bukkit.getWorld("world").getSpawnLocation()
                ));
            }
        }
        register.saveAll();
    }
}
