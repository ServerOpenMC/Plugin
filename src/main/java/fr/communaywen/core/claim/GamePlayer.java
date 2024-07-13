package fr.communaywen.core.claim;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GamePlayer {
    private Player player;

    private Location pos1;
    private Location pos2;

    private int countClaims;

    public static Map<String, GamePlayer> gamePlayers = new HashMap<>();

    public GamePlayer(String playerName) {
        this.player = Bukkit.getPlayer(playerName);
        Team team = AywenCraftPlugin.getInstance().getTeamManager().getTeamByPlayer(player.getUniqueId());

        if(team != null) {
            for(String claimId : AywenCraftPlugin.getInstance().claimConfigFile.get().getKeys(false)) {
                if(team.getName().equals(claimId.substring(0, claimId.length() - 2))) {
                    countClaims++;
                }
            }
        }

        gamePlayers.put(player.getName(), this);
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void setCountClaims(int countClaims) {
        this.countClaims = countClaims;
    }

    public int getCountClaims() {
        return countClaims;
    }

    public static boolean isRegionConflict(Player player, Location minLoc, Location maxLoc) {
        WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
        if (worldGuardPlugin == null) {
            return false;
        }

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(player.getWorld());
        RegionManager regions = container.get(world);

        if (regions == null) {
            return false;
        }

        for (ProtectedRegion existingRegion : regions.getRegions().values()) {
            if (isInside(existingRegion, minLoc) || isInside(existingRegion, maxLoc)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isInside(ProtectedRegion region, Location location) {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= min.getX() && x <= max.getX()
                && y >= min.getY() && y <= max.getY()
                && z >= min.getZ() && z <= max.getZ();
    }
}
