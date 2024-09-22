package fr.communaywen.core.luckyblocks.events.bonus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LBSuperHero extends LuckyBlockEvent {

    public LBSuperHero() {
        super(
                "super_heros",
                "Super-Héros",
                "Effet de résistance IV pendant 1 minute !",
                0.1f,
                EventType.BONUS,
                new ItemStack(Material.SHIELD)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20*60, 3));
    }
}
