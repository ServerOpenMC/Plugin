package fr.communaywen.core.commands.homes;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import jdk.jfr.Name;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;

import java.util.List;

public class SethomesCommands {

    private final HomesManagers homesManagers;

    public SethomesCommands(HomesManagers homesManagers) {
        this.homesManagers = homesManagers;
    }
    @Command("sethome")
    @Description("Définir un point de téléportation")
    public void sethome(Player player, @Named("nom") String name) {
        Location location = player.getLocation();

        // Check if the player set home in the worldguard region


        if (isRegionConflict(player, location)) {
            MessageManager.sendMessageType(player, "§cTu ne peux définir un home dans une région protégée.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if(!location.getWorld().equals(AywenCraftPlugin.getInstance().getServer().getWorlds().get(0))) {
            MessageManager.sendMessageType(player, "§cTu ne peux définir un home que dans le monde principal.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (name.length() < 3) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home doit contenir au moins 3 caractères.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (name.length() > 10) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home ne doit pas dépasser 10 caractères.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (!name.matches("[a-zA-Z0-9]+")) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home ne doit pas contenir de caractères spéciaux.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (name.equalsIgnoreCase("upgrade")) {
            MessageManager.sendMessageType(player, "§cTu ne peux pas nommer ton home comme ça.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }
        int currentHomes = (int) HomesManagers.homes.stream()
                .filter(home -> home.getPlayer().equals(player.getUniqueId().toString()))
                .count();
        int homesLimit = homesManagers.getCurrentHomesLimit(player.getUniqueId());

        if (currentHomes >= homesLimit) {
            MessageManager.sendMessageType(player, "§cTu as atteint ta limite de homes. Améliore ta limite pour en définir plus.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        List<Home> homes = HomesManagers.homes.stream()
                .filter(home -> home.getPlayer().equals(player.getUniqueId().toString()))
                .toList();

        for (Home home : homes) {
            if (home.getLocation().distance(location) < 1) {
                MessageManager.sendMessageType(player, "§cTu as un home à cette endroit: §6" + home.getName(), Prefix.HOME, MessageType.ERROR, true);
                return;
            }

            if (home.getName().equalsIgnoreCase(name)) {
                MessageManager.sendMessageType(player, "§cTu as déjà un home avec ce nom.", Prefix.HOME, MessageType.ERROR, true);
                return;
            }
        }

        homesManagers.addHome(new Home(player.getUniqueId().toString(), name, location));
        MessageManager.sendMessageType(player, "§aHome §e" + name + " §adéfini avec succès !", Prefix.HOME, MessageType.SUCCESS, true);
    }
    public static boolean isRegionConflict(Player player, Location location) {
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
            if (isInside(existingRegion, location)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInside(ProtectedRegion region, Location location) {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= min.x() && x <= max.x()
                && y >= min.y() && y <= max.y()
                && z >= min.z() && z <= max.z();
    }

}
