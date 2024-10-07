package fr.communaywen.core.guideline.listeners.dream;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RootDream implements Listener {
    @EventHandler
    public void onPlayerLoaded(PlayerLoadingCompletedEvent event) {
        UltimateAdvancementAPI api = GuidelineManager.getAPI();
        Player p = event.getPlayer();

        if (api.getAdvancement("dream:root").isGranted(p)) {
            GuidelineManager.getTab().showTab(p);
        }
    }
}
