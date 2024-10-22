package fr.communaywen.core.claim;

import fr.communaywen.core.teams.Team;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegionManager {

    public Location minLoc, maxLoc;
    @Getter
    private Team team;
    @Getter
    private UUID claimID;
    @Getter private final UUID claimer;
    private static final double MIN_Y = -64.0;
    private static final double MAX_Y = 320.0;

    public RegionManager(Location firstPoint, Location secondPoint, Team team, UUID claimer) {
        minLoc = new Location(firstPoint.getWorld(), min(firstPoint.getX(), secondPoint.getX()), min(MIN_Y, MIN_Y), min(firstPoint.getZ(), secondPoint.getZ()));
        maxLoc = new Location(firstPoint.getWorld(), max(firstPoint.getX(), secondPoint.getX()), max(MAX_Y, MAX_Y), max(firstPoint.getZ(), secondPoint.getZ()));
        this.team = team;
        this.claimer = claimer;
        this.claimID = UUID.randomUUID();
    }

    public RegionManager(Location firstPoint, Location secondPoint, Team team, UUID claimID, UUID claimer) {
        minLoc = new Location(firstPoint.getWorld(), min(firstPoint.getX(), secondPoint.getX()), min(MIN_Y, MIN_Y), min(firstPoint.getZ(), secondPoint.getZ()));
        maxLoc = new Location(firstPoint.getWorld(), max(firstPoint.getX(), secondPoint.getX()), max(MAX_Y, MAX_Y), max(firstPoint.getZ(), secondPoint.getZ()));
        this.team = team;
        this.claimID = claimID;
        this.claimer = claimer;
    }

    public double min(double a, double b) {
        return Math.min(a, b);
    }

    public double max(double a, double b) {
        return Math.max(a, b);
    }

    public boolean isInArea(Location loc) {
        if(!minLoc.getWorld().equals(loc.getWorld())) return false;
        return (minLoc.getX() <= loc.getX() && minLoc.getY() <= loc.getY() && minLoc.getZ() <= loc.getZ() && maxLoc.getX() >= loc.getX() && maxLoc.getY() >= loc.getY() && maxLoc.getZ() >= loc.getZ());
    }

    public Location getMiddle() {
        double a, b;
        a = (maxLoc.getX() - minLoc.getX()) / 2D + minLoc.getX();
        b = (maxLoc.getZ() - minLoc.getZ()) / 2D + minLoc.getZ();

        return new Location(Bukkit.getWorld("world"), a, minLoc.getY(), b);
    }

    public List<Location> getArea() {
        List<Location> blocksLocation = new ArrayList<>();

        for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
            for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++)
                    blocksLocation.add(new Location(minLoc.getWorld(), x, y, z));
            }
        }
        return blocksLocation;
    }

    public boolean isTeamMember(UUID playerUuid) {
        return team.isIn(playerUuid);
    }

    @Override
    public String toString() {
        return "RegionManager{" +
                "team=" + team.getName() + "," +
                "claimID=" + getClaimID() +
                '}';
    }


}