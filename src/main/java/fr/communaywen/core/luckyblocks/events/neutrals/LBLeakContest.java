package fr.communaywen.core.luckyblocks.events.neutrals;

import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBLeakContest extends LuckyBlockEvent {

    public LBLeakContest() {
        super(
                "leak_contest",
                "Un Bug du Serveur?",
                "Vous avez réussi à...",
                0.1f,
                EventType.NEUTRAL,
                new ItemStack(Material.EMERALD)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        if (ContestManager.getPhaseCache() == 1) {
            MessageManager.sendMessageType(player, "§9Accès §aAccepté§9... Requête à la base de donnée... " + ChatColor.valueOf(ContestManager.getColor1Cache()) + ContestManager.getCamp1Cache() + " §9VS " + ChatColor.valueOf(ContestManager.getColor2Cache()) + ContestManager.getCamp2Cache(), Prefix.CONTEST, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§9Accès refusé... Vous avez déjà accès au Thème du Contest", Prefix.CONTEST, MessageType.ERROR, true);

        }
    }
}
