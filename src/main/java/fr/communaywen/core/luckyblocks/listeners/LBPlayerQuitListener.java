package fr.communaywen.core.luckyblocks.listeners;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockListeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBPlayerQuitListener implements Listener {

    private final LuckyBlockManager luckyBlockManager;

    public LBPlayerQuitListener(LuckyBlockManager luckyBlockManager) {
        this.luckyBlockManager = luckyBlockManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        luckyBlockManager.getLbEvents().stream()
                .filter(lbe -> lbe instanceof LuckyBlockListeners)
                .forEach(lbe -> ((LuckyBlockListeners) lbe).onPlayerQuit(event));
    }
}
