package fr.communaywen.core.luckyblocks.objects;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface LuckyBlockListeners {

    default void onPlayerQuit(PlayerQuitEvent event) {}
    default void onEntityDeath(EntityDeathEvent event) {}
}
