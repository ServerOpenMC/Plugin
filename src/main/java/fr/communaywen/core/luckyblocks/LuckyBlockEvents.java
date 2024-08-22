package fr.communaywen.core.luckyblocks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

abstract class LuckyBlocksEvents {

    private final String eventName;
    private final String eventDescription;
    private final float eventChance;

    public LuckyBlocksEvents(String eventName, String eventDescription, float eventChance) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventChance = eventChance;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public float getEventChance() {
        return eventChance;
    }

    public abstract void open(Player player, Block luckyBlock);
}
