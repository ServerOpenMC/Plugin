package fr.communaywen.core.levels;

import org.bukkit.entity.Player;

public class LevelsManager {

    public void addExperience(int amount, Player player) {
        LevelsDataManager.addToCount(player, amount);
    }

    public void removeExperience(int amount, Player player) {
        LevelsDataManager.removeToCount(player, amount * -1);
    }

    public void setExperience(int amount, Player player) {
        LevelsDataManager.setCount(player, amount);
    }

    public int getExperience(Player player) {
        return LevelsDataManager.getCount(player);
    }

    public void applyExperienceReward(Player player, String entityType) {
        int rewardAmount = LevelsDataManager.getExperienceReward(entityType);
        boolean rewardsEnabled = LevelsDataManager.rewardsAreEnabled();
        if (rewardsEnabled) {
            LevelsDataManager.addToCount(player, rewardAmount);
            player.sendMessage(" + " + rewardAmount + " xp !");
        } else {
            player.sendMessage("§4les gains d'xp sont désactivés !");
        }
    }

}
