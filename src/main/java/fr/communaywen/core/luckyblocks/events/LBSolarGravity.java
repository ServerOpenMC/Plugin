package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockListeners;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBSolarGravity extends LuckyBlockEvent implements LuckyBlockListeners {

    public LBSolarGravity() {
        super("Gravité solaire!",
                "Gravité Solaire pendant 1 minute !",
                0.5f,
                EventType.MALUS,
                new ItemStack(Material.ORANGE_WOOL)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        // Permet d'afficher le title et de jouer un son lors de l'ouverture NE PAS L'OUBLIER
        super.onOpen(player, block);
        player.getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(1f);

        new BukkitRunnable() {
            public void run() {
                player.getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
            }
        }.runTaskLater(AywenCraftPlugin.getInstance(), 20 * 60);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
    }
}
