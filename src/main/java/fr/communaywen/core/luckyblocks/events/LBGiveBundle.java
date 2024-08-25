package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBGiveBundle extends LuckyBlockEvent {

    public LBGiveBundle() {
        super("Rangement insolite",
                "Vous avez obtenu un bundle !",
                0.7f
        );
    }

    public void onOpen(Player player, Block block) {

        ItemStack bundle = new ItemStack(Material.BUNDLE);
        block.getWorld().dropItemNaturally(block.getLocation(), bundle);
    }
}
