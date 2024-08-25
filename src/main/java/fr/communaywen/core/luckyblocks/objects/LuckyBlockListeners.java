package fr.communaywen.core.luckyblocks.objects;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.event.player.PlayerQuitEvent;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public interface LuckyBlockListeners {

    default void onPlayerQuit(PlayerQuitEvent event) {}
}
