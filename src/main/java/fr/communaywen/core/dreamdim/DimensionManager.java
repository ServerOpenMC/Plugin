package fr.communaywen.core.dreamdim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.dreamdim.fishing.FishingListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.communaywen.core.dreamdim.populators.*;
import fr.communaywen.core.dreamdim.listeners.*;
import fr.communaywen.core.dreamdim.enchantments.*;

import java.util.Objects;

@Feature("Dream Dimension")
@Credit("Gyro3630")
@Collaborators({"ri1_", "Nocolm"})
public class DimensionManager implements Listener {

    AywenCraftPlugin plugin;
    Server server;
    AdvancementRegister register;
    CloudSoup cloudSoup;

    public DimensionManager(AywenCraftPlugin Plugin) {
        this.plugin = Plugin;
        this.server = plugin.getServer();
    }

    public void init() {
        createDimension();
        this.register = new AdvancementRegister(plugin);

        this.cloudSoup = new CloudSoup(plugin, register);

        plugin.registerEvents(
                new DreamSlayer(),
                new Coma(plugin),

                new StripWood(),
                new FishingListener(plugin),
                new CodexSomnii(register),
                cloudSoup,
                new MilkListener(),
                new BlockPlacement(register),
                new EatListener(plugin),
                new EnterWorldListener(plugin, register),
                new DisableSculk(),
                new MobListener(),
                new TreePlacer()
        );
    }

    public void createDimension() {
        WorldCreator creator = new WorldCreator("dreamworld");
        creator.biomeProvider(new DreamBiomeProvider(creator.seed()));
        creator.generator(new DreamChunkGenerator());
        creator.environment(World.Environment.NORMAL);
        World dreamworld = creator.createWorld();

        dreamworld.getPopulators().add(new LightPopulator());
        dreamworld.getPopulators().add(new TreePopulator());

        dreamworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        dreamworld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        dreamworld.setGameRule(GameRule.DISABLE_RAIDS, true);
        dreamworld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        dreamworld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        dreamworld.setGameRule(GameRule.NATURAL_REGENERATION, false);

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
        this.cloudSoup.close();
    }
}
