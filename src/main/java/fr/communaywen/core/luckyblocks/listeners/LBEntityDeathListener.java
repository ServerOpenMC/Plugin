package fr.communaywen.core.luckyblocks.listeners;

import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockListeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class LBEntityDeathListener implements Listener {

    private final LuckyBlockManager luckyBlockManager;

    public LBEntityDeathListener(LuckyBlockManager luckyBlockManager) {
        this.luckyBlockManager = luckyBlockManager;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        luckyBlockManager.getLbEvents().stream()
                .filter(lbe -> lbe instanceof LuckyBlockListeners)
                .forEach(lbe -> ((LuckyBlockListeners) lbe).onEntityDeath(event));
    }
}
