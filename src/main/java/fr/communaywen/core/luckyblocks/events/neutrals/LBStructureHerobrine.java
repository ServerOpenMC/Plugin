package fr.communaywen.core.luckyblocks.events.neutrals;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.luckyblocks.utils.Structure;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class LBStructureHerobrine extends LuckyBlockEvent {

    private final File file = Structure.getStructureFile("lb_herobrine");

    public LBStructureHerobrine() {
        super(
                "structure_herobrine",
                "Un vieil ami",
                "Apparition de la structure d'Herobrine !",
                0.4f,
                EventType.NEUTRAL,
                new ItemStack(Material.NETHERRACK)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        // Permet d'afficher le title et de jouer un son lors de l'ouverture NE PAS L'OUBLIER
        super.onOpen(player, block);

        try {
            Structure.placeStructure(file, block.getLocation(), false, false, player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
