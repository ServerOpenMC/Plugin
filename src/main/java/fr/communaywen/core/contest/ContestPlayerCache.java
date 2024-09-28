package fr.communaywen.core.contest;

import org.bukkit.ChatColor;

public class ContestPlayerCache {
    private final int points;
    private final int camp;
    private final ChatColor color;
    private final long cacheTime;

    public ContestPlayerCache(int points, int camp, ChatColor color) {
        this.points = points;
        this.camp = camp;
        this.color = color;
        this.cacheTime = System.currentTimeMillis();
    }

    public int getPoints() {
        return points;
    }

    public int getCamp() {
        return camp;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isCacheNull(long cacheDuration) {
        return (System.currentTimeMillis() - cacheTime) > cacheDuration;
    }
}
