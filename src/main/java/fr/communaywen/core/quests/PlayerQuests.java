package fr.communaywen.core.quests;

import java.util.HashMap;
import java.util.Map;

public class PlayerQuests {

    private final Map<QuestsManager.QUESTS, Integer> questsProgress;

    public PlayerQuests() {
        this.questsProgress = new HashMap<>();
        for (QuestsManager.QUESTS quest : QuestsManager.QUESTS.values()) {
            questsProgress.put(quest, 0);
        }
    }

    public Map<QuestsManager.QUESTS, Integer> getQuestsProgress() {
        return questsProgress;
    }

    public int getProgress(QuestsManager.QUESTS quest) {
        return questsProgress.getOrDefault(quest, 0);
    }

    public void addProgress(QuestsManager.QUESTS quest, int amount) {
        int newProgress = questsProgress.getOrDefault(quest, 0) + amount;
        questsProgress.put(quest, newProgress);
    }

    public boolean isQuestCompleted(QuestsManager.QUESTS quest) {
        return getProgress(quest) >= quest.getQt();
    }

}
