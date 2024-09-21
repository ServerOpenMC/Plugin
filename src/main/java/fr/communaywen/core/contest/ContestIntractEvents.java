package fr.communaywen.core.contest;

import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.util.Objects;

public class ContestIntractEvents implements Listener {
    @EventHandler
    private void onFurnitureInteractEvent(FurnitureInteractEvent furniture) {
        if (Objects.equals(furniture.getNamespacedID(), "contest:borne")) {
            furniture.getPlayer().playSound(furniture.getPlayer().getLocation(), Sound.BLOCK_BARREL_OPEN, 1.0F, 0.7F);
            Bukkit.dispatchCommand(furniture.getPlayer(), "aywencraftplugin:contest");
        }
    }
}
