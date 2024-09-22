package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Credit("Fnafgameur")
public class LBSuperZero extends LuckyBlockEvent {

    public LBSuperZero() {
        super(
                "super_zero",
                "Super-Zéro",
                "Effet d'unluck pendant 1 minute !",
                0.4f,
                EventType.MALUS,
                new ItemStack(Material.FERN)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 20*60, 3));
    }
}
