package fr.communaywen.core.luckyblocks.managers;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;

import java.util.HashMap;
import java.util.UUID;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LBPlayerManager {

    private final long timeToWait = 86400000; // = 1 jour
    private final HashMap<UUID, Long> luckyBlocksCooldown = new HashMap<>();

    public void setLuckyBlocksCooldown(UUID uuid, long time) {
        luckyBlocksCooldown.put(uuid, time);
    }

    public long getLuckyBlocksCooldown(UUID uuid) {
        return luckyBlocksCooldown.getOrDefault(uuid, 0L);
    }

    public boolean canClaimLuckyBlocks(UUID uuid) {
        if (System.currentTimeMillis() - getLuckyBlocksCooldown(uuid) < timeToWait) {
            return false;
        }

        setLuckyBlocksCooldown(uuid, System.currentTimeMillis());
        return true;
    }

    public int getRemainingHours(UUID uuid) {
        return (int) ((timeToWait - (System.currentTimeMillis() - getLuckyBlocksCooldown(uuid))) / 3600000);
    }

    public int getRemainingMinutes(UUID uuid) {
        return (int) ((timeToWait - (System.currentTimeMillis() - getLuckyBlocksCooldown(uuid))) / 60000) % 60;
    }

    public int getRemainingSeconds(UUID uuid) {
        return (int) ((timeToWait - (System.currentTimeMillis() - getLuckyBlocksCooldown(uuid))) / 1000) % 60;
    }
}
