package fr.communaywen.core.contest;

import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.util.Objects;

public class ContestIntractEvents implements Listener {
    @EventHandler
    private void onFurnitureInteractEvent(FurnitureInteractEvent furniture) {
        if (Objects.equals(furniture.getNamespacedID(), "contest:borne")) {
            Bukkit.dispatchCommand(furniture.getPlayer(), "aywencraftplugin:contest");
        }
    }
}
