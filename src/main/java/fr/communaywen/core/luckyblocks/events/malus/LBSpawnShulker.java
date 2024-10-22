package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBSpawnShulker extends LuckyBlockEvent {

    public LBSpawnShulker() {
        super(
                "spawn_shulker",
                "Wingardiuuuum Levioosaaaaa",
                "Un Shulker est apparu !",
                0.1f,
                EventType.MALUS,
                new ItemStack(Material.SHULKER_BOX)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        // Permet d'afficher le title et de jouer un son lors de l'ouverture NE PAS L'OUBLIER
        super.onOpen(player, block);
        block.getWorld().spawnEntity(block.getLocation(), EntityType.SHULKER);
    }
}
