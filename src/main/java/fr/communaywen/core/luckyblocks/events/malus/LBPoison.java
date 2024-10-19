package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LBPoison extends LuckyBlockEvent {

    public LBPoison() {
        super(
                "poison",
                "Aïe...",
                "Effet de poison pendant 20 secondes !",
                0.4f,
                EventType.MALUS,
                new ItemStack(Material.POISONOUS_POTATO)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 0));
    }
}
