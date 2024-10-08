package fr.communaywen.core.contest.cache;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.managers.ContestManager;

public class ContestCache {

    private static ContestManager contestManager;
    public ContestCache(ContestManager manager) {
        contestManager = manager;
    }

    private static String camp1Cache = null;
    private static long lastCamp1Update = 0;
    private static String camp2Cache = null;
    private static long lastCamp2Update = 0;
    private static String color1Cache = null;
    private static long lastColor1Update = 0;
    private static String color2Cache = null;
    private static long lastColor2Update = 0;
    private static String startDateCache = null;
    private static long lastStartDateUpdate = 0;
    private static Integer phaseCache = null;
    private static long lastPhaseUpdate = 0;
    private static final long cacheDuration = 120000;

    public static String getCamp1Cache() {
        long currentTime = System.currentTimeMillis();

        if (camp1Cache == null || (currentTime - lastCamp1Update) > cacheDuration) {
            camp1Cache = contestManager.getString("contest", "camp1");
            lastCamp1Update = currentTime;
        }

        return camp1Cache;
    }

    public static String getCamp2Cache() {
        long currentTime = System.currentTimeMillis();

        if (camp2Cache == null || (currentTime - lastCamp2Update) > cacheDuration) {
            camp2Cache = contestManager.getString("contest", "camp2");
            lastCamp2Update = currentTime;
        }

        return camp2Cache;
    }

    public static String getColor1Cache() {
        long currentTime = System.currentTimeMillis();

        if (color1Cache == null || (currentTime - lastColor1Update) > cacheDuration) {
            color1Cache = contestManager.getString("contest", "color1");
            lastColor1Update = currentTime;
        }

        return color1Cache;
    }

    public static String getColor2Cache() {
        long currentTime = System.currentTimeMillis();

        if (color2Cache == null || (currentTime - lastColor2Update) > cacheDuration) {
            color2Cache = contestManager.getString("contest", "color2");
            lastColor2Update = currentTime;
        }

        return color2Cache;
    }
    public static int getPhaseCache() {
        long currentTime = System.currentTimeMillis();
        if (phaseCache == null || (currentTime - lastPhaseUpdate) > cacheDuration) {
            phaseCache = contestManager.getInt("contest", "phase");
            lastPhaseUpdate = currentTime;
        }
        return phaseCache;
    }
    public static String getStartDateCache() {
        long currentTime = System.currentTimeMillis();

        if (startDateCache == null || (currentTime - lastStartDateUpdate) > cacheDuration) {
            startDateCache = contestManager.getString("contest", "startdate");
            lastStartDateUpdate = currentTime;
        }

        return startDateCache;
    }
}
