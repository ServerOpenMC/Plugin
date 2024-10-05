package fr.communaywen.core.spawn.head;

import org.bukkit.ChatColor;

import java.util.List;

public class HeadPlayerCache {
    private final List<String> headsFound;
    private final long cacheTime;

    public HeadPlayerCache(List<String> headsFound) {
        this.headsFound = headsFound;
        this.cacheTime = System.currentTimeMillis();
    }

    public List<String> getHeadsFound() {
        return headsFound;
    }

    public boolean isCacheNull(long cacheDuration) {
        return (System.currentTimeMillis() - cacheTime) > cacheDuration;
    }
}
