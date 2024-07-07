package fr.communaywen.core.commands;

import fr.communaywen.core.utils.FallingBlocksExplosion;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FBoomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return false;
        }

        Location location = new Location(((Player) sender).getPlayer().getWorld(), ((Player) sender).getPlayer().getLocation().getBlockX(), ((Player) sender).getPlayer().getLocation().getBlockY(), ((Player) sender).getPlayer().getLocation().getBlockZ());

        new FallingBlocksExplosion(20, location, true);

        return false;
    }
}
