package fr.communaywen.core.luckyblocks.objects;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
/*
* Credit to "mcross_bow_voc" for the idea
* Credit to every member that'll contribute by adding new events
*/
@Getter
public abstract class LuckyBlockEvent {

    private final String name;
    private final String description;
    private final float chance;

    public LuckyBlockEvent(String name, String description, float chance) {
        this.name = name;
        this.description = description;
        this.chance = chance;
    }

    public void triggerOpen(Player player, Block block) {

        // Affichage du nom de l'event pendant 1 seconde
        player.sendTitle("", "§6§l" + name, 8, 20, 8);
        // Affichage de la description de l'event
        player.sendMessage("§8[§bLucky Block§8]§7 " + description);
        onOpen(player, block);
    }

    public abstract void onOpen(Player player, Block block);
}
