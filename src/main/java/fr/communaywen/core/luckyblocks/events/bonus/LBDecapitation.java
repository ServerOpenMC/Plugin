package fr.communaywen.core.luckyblocks.events.bonus;

import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBDecapitation extends LuckyBlockEvent {

    public LBDecapitation() {
        super(
                "decapitation",
                "DÉCAPITATION !!!",
                "Vous obtenez votre tête !",
                0.01f,
                EventType.BONUS,
                new ItemStack(Material.PLAYER_HEAD)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        World world = block.getWorld();
        ItemStack playerHead = ItemUtils.getPlayerSkull(player.getUniqueId());

        world.dropItemNaturally(block.getLocation(), playerHead);
    }
}
