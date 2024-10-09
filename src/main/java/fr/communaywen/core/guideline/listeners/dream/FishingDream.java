package fr.communaywen.core.guideline.listeners.dream;

import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingDream implements Listener {
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals("dreamworld")) return;
        if (event.getState().equals(PlayerFishEvent.State.FISHING)) {
            GuidelineManager.getAPI().getAdvancement("dream:fishing").grant(player);
        }
    }
}
