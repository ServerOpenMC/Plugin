package fr.communaywen.core.luckyblocks.events.neutrals;

import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBLeakContest extends LuckyBlockEvent {
    private final ContestManager contestManager;
    public LBLeakContest(ContestManager contestManager) {
        super(
                "leak_contest",
                "Un Bug du Serveur?",
                "Vous avez réussi à...",
                0.1f,
                EventType.NEUTRAL,
                new ItemStack(Material.EMERALD)
        );
        this.contestManager = contestManager;
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        if (ContestCache.getPhaseCache() == 1) {
            MessageManager.sendMessageType(player, "§9Accès §aAccepté§9... Requête à la base de donnée... " + ChatColor.valueOf(ContestCache.getColor1Cache()) + ContestCache.getCamp1Cache() + " §9VS " + ChatColor.valueOf(ContestCache.getColor2Cache()) + ContestCache.getCamp2Cache(), Prefix.CONTEST, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§9Accès refusé... Vous avez déjà accès au Thème du Contest", Prefix.CONTEST, MessageType.ERROR, true);

        }
    }
}
