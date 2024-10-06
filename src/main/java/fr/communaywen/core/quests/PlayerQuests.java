package fr.communaywen.core.quests;

import fr.communaywen.core.quests.qenum.QUESTS;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerQuests {

    private final Map<QUESTS, Integer> questsProgress;
    public final Map<QUESTS, Integer> questsTier;

    public PlayerQuests() {
        this.questsProgress = new HashMap<>();
        this.questsTier = new HashMap<>();
        for (QUESTS quest : QUESTS.values()) {
            questsProgress.put(quest, 0);
            questsTier.put(quest, 0);
        }
    }

    public int getProgress(QUESTS quest) {
        return questsProgress.getOrDefault(quest, 0);
    }

    public int getCurrentTier(QUESTS quest) {
        return questsTier.getOrDefault(quest, 0);
    }

    public void setCurrentTier(QUESTS quests, int tier) {
        questsTier.put(quests, tier);
    }

    public void setProgress(QUESTS quest, int progress) {
        questsProgress.put(quest, progress);
        int tier = 0;
        for (int i = 0; i < quest.getQtTiers().length; i++) {
            if (progress >= quest.getQt(i)) {
                tier = i + 1;
                setCurrentTier(quest, tier);
            } else {
                break;
            }
        }
    }

    public void addProgress(QUESTS quest, int amount) {
        int currentTier = getCurrentTier(quest);
        if(currentTier < 0 || currentTier >= quest.getQtTiers().length)
            throw new IllegalArgumentException("Invalid tier for quest " + quest.name());
        int newProgress = questsProgress.getOrDefault(quest, 0) + amount;
        questsProgress.put(quest, newProgress);

        if (newProgress >= quest.getQt(currentTier) && currentTier < quest.getQtTiers().length - 1) {
            questsTier.put(quest, currentTier + 1);
        }
    }

    public boolean isQuestCompleted(QUESTS quest) {
        int currentTier = getCurrentTier(quest);
        return currentTier >= quest.getQtTiers().length - 1 && getProgress(quest) >= quest.getQt(quest.getQtTiers().length - 1);
    }

    public void removeProgress(QUESTS quest, int amount) {
        int currentProgress = questsProgress.getOrDefault(quest, 0);
        int newProgress = Math.max(0, currentProgress - amount);
        questsProgress.put(quest, newProgress);

        int tier = 0;
        for (int i = 0; i < quest.getQtTiers().length; i++) {
            if (newProgress >= quest.getQt(i)) {
                tier = i + 1;
                setCurrentTier(quest, tier);
            } else {
                break;
            }
        }
    }

    public void resetProgress(QUESTS quest) {
        questsProgress.put(quest, 0);
        setCurrentTier(quest, 0);
    }

    public void calculateAllTiers() {
        for (QUESTS quest : QUESTS.values()) {
            int progress = getProgress(quest);
            int tier = 0;
            for (int i = 0; i < quest.getQtTiers().length; i++) {
                if (progress >= quest.getQt(i)) {
                    tier = i + 1;
                } else {
                    break;
                }
            }
            setCurrentTier(quest, tier);
        }
    }
}