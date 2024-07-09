package fr.communaywen.core.levels;

import org.bukkit.entity.Player;

public class ExperienceManager {

    public void addExperience(int amount, Player player) {
        LevelsDataManager.addToCount(player, amount);
    }

    public void removeExperience(int amount, Player player) {
        LevelsDataManager.addToCount(player, amount * -1);
    }

    public void setExperience(int amount, Player player) {
        LevelsDataManager.setCount(player, amount);
    }

}
