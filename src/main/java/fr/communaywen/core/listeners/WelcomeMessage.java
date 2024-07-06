package fr.communaywen.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// show the title "Merci de ne pas grief" at the player's first connection

public class WelcomeMessage implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPlayedBefore()) {
            player.sendTitle("Bienvenue !", "Merci de ne pas grief", 10, 70, 20);
        }
    }

}
