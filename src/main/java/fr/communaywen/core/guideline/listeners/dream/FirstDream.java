package fr.communaywen.core.guideline.listeners.dream;

import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class FirstDream implements Listener {
    @EventHandler
    public void onChangeDimension(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("dreamworld")) {
            GuidelineManager.getAPI().getAdvancement("openmc:firstdream").grant(e.getPlayer());
        }
    }
}
