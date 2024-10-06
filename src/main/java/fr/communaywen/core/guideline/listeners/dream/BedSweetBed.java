package fr.communaywen.core.guideline.listeners.dream;

import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BedSweetBed implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!(event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND))) return;
        if (!(event.getFrom().getWorld().getName().equals("dreamworld"))) return;

        GuidelineManager.getAPI().getAdvancement("aywen:bedsweetbed").grant(event.getPlayer());
    }
}
