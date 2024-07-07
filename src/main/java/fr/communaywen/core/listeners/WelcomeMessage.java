package fr.communaywen.core.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// show the title "Merci de ne pas grief" at the player's first connection

public class WelcomeMessage implements Listener {

    private final FileConfiguration welcomeMessageConfig;

    public WelcomeMessage(FileConfiguration welcomeMessageConfig) {
        this.welcomeMessageConfig = welcomeMessageConfig;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPlayedBefore() || player.getName().equals("Henri269")) {
            player.sendTitle(welcomeMessageConfig.getString("title"),
                    welcomeMessageConfig.getString("subtitle"),
                    welcomeMessageConfig.getInt("fadeIn"),
                    welcomeMessageConfig.getInt("stay"),
                    welcomeMessageConfig.getInt("fadeOut"));
        }
    }

}
