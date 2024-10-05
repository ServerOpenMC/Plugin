package fr.communaywen.core.guideline;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GrantRoot implements Listener {
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        GuidelineManager.getAPI().getAdvancement("openmc:root").grant(event.getPlayer());
    }
}