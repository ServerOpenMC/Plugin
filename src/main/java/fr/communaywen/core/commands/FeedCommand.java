package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.UUID;

public class FeedCommand {
    private final long COOLDOWN_TIME;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public FeedCommand(@NotNull AywenCraftPlugin plugin) {
        COOLDOWN_TIME = plugin.getConfig().getLong("feed.cooldown");
    }
    @Command("feed")
    @CommandPermission("ayw.command.feed")
    public void onCommand(Player player) {
        UUID playerId = player.getUniqueId();
        long Time = System.currentTimeMillis() / 1000;

        if (cooldowns.containsKey(playerId)) {
            long lastUsed = cooldowns.get(playerId);
            long timeSinceLastUse = Time - lastUsed;

            if (timeSinceLastUse < COOLDOWN_TIME) {
                long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                return;
            }
        }

        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setExhaustion(0);
        player.sendMessage("Vous avez été nouris \uE032"); //émoji baguette
        cooldowns.put(playerId, Time);
    }
}