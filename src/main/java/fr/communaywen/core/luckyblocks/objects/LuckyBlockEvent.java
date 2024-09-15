package fr.communaywen.core.luckyblocks.objects;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
/*
* Credit to "mcross_bow_voc" for the idea
* Credit to every member that'll contribute by adding new events
*/
@Getter
public class LuckyBlockEvent {

    private final String name;
    private final String description;
    private final double chance;
    private final EventType eventType;
    private final ItemStack iconItem;

    public LuckyBlockEvent(String name, String description, double chance, EventType eventType, ItemStack iconItem) {
        this.name = name;
        this.description = description;
        this.chance = chance;
        this.eventType = eventType;
        this.iconItem = iconItem;
    }

    public void onOpen(Player player, Block block) {
        Prefix prefix = eventType.getPrefix();
        ChatColor color = eventType.getColor();

        // Affichage du nom de l'event pendant 1 seconde en subtitle
        player.sendTitle("", color + "§l" + name, 8, 20, 8);

        // Affichage de la description de l'event dans le chat
        MessageManager.sendMessageType(player, description, prefix, MessageType.INFO, false);

        // Joue un son aléatoire en fonction du type de l'event
        Sound soundToPlay = EventType.getRandomSoundByType(eventType);
        block.getWorld().playSound(block.getLocation(), soundToPlay , 1, 1);
    }
}
