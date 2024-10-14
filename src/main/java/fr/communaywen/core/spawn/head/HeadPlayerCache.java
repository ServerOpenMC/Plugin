package fr.communaywen.core.spawn.head;

import org.bukkit.ChatColor;

import java.util.List;

public class HeadPlayerCache {
    private final List<String> headsFound;
    private final int headFoundInt;
    private final long cacheTime;

    public HeadPlayerCache(List<String> headsFound, int headFoundInt) {
        this.headsFound = headsFound;
        this.headFoundInt = headFoundInt;
        this.cacheTime = System.currentTimeMillis();
    }

    public List<String> getHeadsFound() {
        return headsFound;
    }

    public int getHeadsFoundInt() {
        return headFoundInt;
    }

    public boolean isCacheNull(long cacheDuration) {
        return (System.currentTimeMillis() - cacheTime) > cacheDuration;
    }
}
