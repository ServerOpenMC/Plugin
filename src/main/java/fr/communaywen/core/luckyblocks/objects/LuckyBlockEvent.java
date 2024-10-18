package fr.communaywen.core.luckyblocks.objects;

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

/*
* Credit to "mcross_bow_voc" for the idea
* Credit to every member that'll contribute by adding new events
*/
@Getter
public class LuckyBlockEvent {

    private final String id;
    private final String name;
    private final String description;
    private final double chance;
    private final EventType eventType;
    private final ItemStack iconItem;

    public LuckyBlockEvent(String id, String name, String description, double chance, EventType eventType, ItemStack iconItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.chance = chance;
        this.eventType = eventType;
        this.iconItem = iconItem;
    }

    public void onOpen(Player player, Block block) {
        Prefix prefix = eventType.getPrefix();
        ChatColor color = eventType.getColor();
        Sound soundToPlay = EventType.getRandomSoundByType(eventType);

        player.sendTitle("", color + "§l" + name, 8, 20, 8);
        MessageManager.sendMessageType(player, description, prefix, MessageType.INFO, false);
        block.getWorld().playSound(block.getLocation(), soundToPlay , 1, 1);
    }
}
