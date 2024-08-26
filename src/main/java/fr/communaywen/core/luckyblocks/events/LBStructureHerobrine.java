package fr.communaywen.core.luckyblocks.events;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.luckyblocks.utils.Structure;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBStructureHerobrine extends LuckyBlockEvent {

    public LBStructureHerobrine() {
        super("Un vieil ami",
                "Apparition de la structure d'Herobrine !",
                0.7f,
                EventType.NEUTRAL
        );
    }

    @Override
    public void onOpen(Player player, Block block) {

        File file = Structure.getStructureFile("herobrine");

        try {
            Structure.placeStructure(file, block.getLocation(), false, false, player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
