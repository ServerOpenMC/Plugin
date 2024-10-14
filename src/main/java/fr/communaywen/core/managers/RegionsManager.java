package fr.communaywen.core.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionsManager {

    public static boolean isPlayerInRegion(String regionId, World world) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        com.sk89q.worldguard.protection.managers.RegionManager regionManager = container.get(BukkitAdapter.adapt(world));

        if (regionManager == null) {
            return false;
        }

        ProtectedRegion region = regionManager.getRegion(regionId);
        if (region == null) {
            return false;
        }

        List<Player> playersInWorld = world.getPlayers();
        for (Player player : playersInWorld) {
            Location playerLocation = player.getLocation();
            BlockVector3 playerVector = BukkitAdapter.asBlockVector(playerLocation);

            if (region.contains(playerVector)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSpecifiedPlayerInRegion(Player player, String regionName) {
        Location playerLocation = player.getLocation();
        World world = playerLocation.getWorld();

        if (world == null) {
            return false;
        }

        com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        if (container == null) {
            return false;
        }

        RegionManager regions = container.get(wgWorld);

        if (regions == null) {
            return false;
        }

        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(playerLocation);

        ApplicableRegionSet regionSet = regions.getApplicableRegions(wgLocation.toVector().toBlockPoint());

        for (ProtectedRegion region : regionSet) {
            if (region.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }

        return false;
    }
}
