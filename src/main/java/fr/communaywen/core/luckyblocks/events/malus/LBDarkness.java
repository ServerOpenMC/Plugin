package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LBDarkness extends LuckyBlockEvent {

    public LBDarkness() {
        super(
                "darkness",
                "Aveugle !",
                "Effet de darkness pendant 1 minute !",
                0.3f,
                EventType.MALUS,
                new ItemStack(Material.WARDEN_SPAWN_EGG)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*60, 0));
    }
}
