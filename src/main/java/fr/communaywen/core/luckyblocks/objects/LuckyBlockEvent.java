package fr.communaywen.core.luckyblocks.objects;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import lombok.Getter;
import org.bukkit.Sound;
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
    private final EventType eventType;

    public LuckyBlockEvent(String name, String description, float chance, EventType eventType) {
        this.name = name;
        this.description = description;
        this.chance = chance;
        this.eventType = eventType;
    }

    public void triggerOpen(Player player, Block block) {

        String color = switch (eventType) {
            case BONUS -> "§a";
            case MALUS -> "§c";
            default -> "§7";
        };

        // Affichage du nom de l'event pendant 1 seconde en subtitle
        player.sendTitle("", color + "§l" + name, 8, 20, 8);
        // Affichage de la description de l'event dans le chat
        player.sendMessage("§8[§bLucky Block §8- " + color + eventType.getName() + "§8]§7 " + description);

        // Joue un son aléatoire en fonction du type de l'event
        final Sound soundToPlay = EventType.getRandomSoundByType(eventType);
        block.getWorld().playSound(block.getLocation(), soundToPlay , 1, 1);

        onOpen(player, block);
    }

    public abstract void onOpen(Player player, Block block);
}
