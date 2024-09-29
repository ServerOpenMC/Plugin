package fr.communaywen.core.personalhome;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.personalhome.listeners.BagInteraction;
import fr.communaywen.core.personalhome.listeners.BuildRestrictions;
import fr.communaywen.core.personalhome.listeners.PreventFall;
import fr.communaywen.core.utils.database.DatabaseConnector;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HomeManager extends DatabaseConnector implements Listener {
    AywenCraftPlugin plugin;
    World homeWorld;

    public HomeManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Getter
    public HashMap<UUID, Home> homes = new HashMap<>();

    public void assign(Player player) throws SQLException {
        Home home = new Home(player.getUniqueId(), homes.size()+1);
        home.save(connection);
        homes.put(player.getUniqueId(), home);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        if (!homes.containsKey(player.getUniqueId())) {
            assign(player);
        }
    }

    public void init() {
        homeWorld = createDimension();

        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM personal_homes").executeQuery();

            while (rs.next()) {
                Home home = new Home(
                        UUID.fromString(rs.getString("player")),
                        rs.getInt("id")
                );
                @Nullable String spawnpoint = rs.getString("spawnpoint");
                if (spawnpoint != null) {
                    int[] coords = HomesUtils.deserializeCoords(spawnpoint);
                    if (coords != null) {
                        Location location = new Location(homeWorld, coords[0], coords[1], coords[2]);
                        if (coords.length == 5) {
                            location.setYaw(coords[3]);
                            location.setPitch(coords[4]);
                        }
                        home.setSpawnpoint(location);
                    }
                }

                Biome biome = Biome.valueOf(rs.getString("biome"));
                home.setBiome(biome);
                home.allow_visit = rs.getBoolean("allow_visit");

                homes.put(home.getOwner(), home);
            }
        } catch (Exception e){
            e.printStackTrace();
            plugin.getLogger().severe("Impossible de charger les maisons :'(");
        }

        plugin.registerEvents(
                new PreventFall(),
                new BuildRestrictions(),
                new BagInteraction(homes),
                this
        );
    }

    private World createDimension() {
        WorldCreator creator = new WorldCreator("homes");
        creator.generator(new HomeChunkGenerator());
        creator.environment(World.Environment.NORMAL);
        World homes = creator.createWorld();

        // Invincibles
        homes.setGameRule(GameRule.FALL_DAMAGE, false);
        homes.setGameRule(GameRule.DROWNING_DAMAGE, false);
        homes.setGameRule(GameRule.FIRE_DAMAGE, true);
        homes.setGameRule(GameRule.FREEZE_DAMAGE, false);

        homes.setGameRule(GameRule.DISABLE_RAIDS, true);
        homes.setGameRule(GameRule.DO_INSOMNIA, false);
        homes.setGameRule(GameRule.MOB_GRIEFING, false);
        homes.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        homes.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        homes.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        homes.setGameRule(GameRule.DO_TRADER_SPAWNING, false);

        plugin.getLogger().info("Homes dimensions created successfully!");
        return homes;
    }
}
