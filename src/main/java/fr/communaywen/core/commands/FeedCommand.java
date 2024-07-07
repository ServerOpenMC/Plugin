package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class FeedCommand implements CommandExecutor {
    private final long COOLDOWN_TIME;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public FeedCommand(@NotNull AywenCraftPlugin plugin) {
        COOLDOWN_TIME = plugin.getConfig().getLong("feed.cooldown");
    }
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final String[] args) {
        if (sender instanceof Player player) {
            UUID playerId = player.getUniqueId();
            long Time = System.currentTimeMillis() / 1000;

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = Time - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                    return true;
                }
            }
            player.setFoodLevel(20);
            player.setSaturation(5);
            player.setExhaustion(0);
            player.sendMessage("Vous avez été nouris \uE032"); //émoji baguette
            cooldowns.put(playerId, Time);
            return true;
        }
        return false;
    }
}