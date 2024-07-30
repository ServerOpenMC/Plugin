
package com.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class HealPlugin extends JavaPlugin {

    private final Map<String, Long> cooldowns = new HashMap<>();
    private final int COOLDOWN_TIME = 60; // Cooldown en secondes

    @Override
    public void onEnable() {
        this.getCommand("heal").setExecutor(new HealCommand());
    }

    @Override
    public void onDisable() {
    }

    public class HealCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                long currentTime = System.currentTimeMillis() / 1000L;
                String playerName = player.getName();

                if (cooldowns.containsKey(playerName)) {
                    long lastUsed = cooldowns.get(playerName);
                    long timeElapsed = currentTime - lastUsed;

                    if (timeElapsed < COOLDOWN_TIME) {
                        long timeRemaining = COOLDOWN_TIME - timeElapsed;
                        player.sendMessage("Vous devez attendre encore " + timeRemaining + " secondes avant de pouvoir utiliser cette commande.");
                        return true;
                    }
                }

                // Effectuer l'action de la commande
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.sendMessage("Vous avez été soigné.");

                // Mettre à jour le temps du cooldown
                cooldowns.put(playerName, currentTime);
            } else {
                sender.sendMessage("Cette commande ne peut être utilisée que par des joueurs.");
            }
            return true;
        }
    }
}
