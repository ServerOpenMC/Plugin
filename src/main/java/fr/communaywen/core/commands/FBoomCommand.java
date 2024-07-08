package fr.communaywen.core.commands;

import fr.communaywen.core.utils.FallingBlocksExplosion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class FBoomCommand {
    @Command("fboom")
    @Description("Crée une explosion de falling blocks autour du joueur")
    @CommandPermission("openmc.command.fboom")
    public void onCommand(Player player) {
        Location location = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getPlayer().getLocation().getBlockZ());

        new FallingBlocksExplosion(20, location, true);
    }
}
