package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LBProut extends LuckyBlockEvent {

    public LBProut() {
        super("Oh Shit...",
                "Tous les joueurs executent '/prout' !",
                0.7f,
                EventType.NEUTRAL
        );
    }

    @Override
    public void onOpen(Player player, Block block) {

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.performCommand("prout");
        }
    }
}
