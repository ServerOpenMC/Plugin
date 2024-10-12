package fr.communaywen.core.contest.cache;

public class ContestDataCache {
    private final String camp1;
    private final String camp2;
    private final String color1;
    private final String color2;
    private final int phase;
    private final String startdate;
    private final long cacheTime;

    public ContestDataCache(String camp1, String camp2, String color1, String color2, int phase, String startdate) {
        this.camp1 = camp1;
        this.camp2 = camp2;
        this.color1 = color1;
        this.color2 = color2;
        this.phase = phase;
        this.startdate = startdate;

        this.cacheTime = System.currentTimeMillis();
    }

    public String getCamp1() {
        return camp1;
    }

    public String getCamp2() {
        return camp2;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public int getPhase() {
        return phase;
    }

    public String getStartDate() {
        return startdate;
    }

    public boolean isCacheNull(long cacheDuration) {
        return (System.currentTimeMillis() - cacheTime) > cacheDuration;
    }
}
