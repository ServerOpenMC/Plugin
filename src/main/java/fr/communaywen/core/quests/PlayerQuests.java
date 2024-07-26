package fr.communaywen.core.quests;

import fr.communaywen.core.quests.qenum.QUESTS;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerQuests {

    private final Map<QUESTS, Integer> questsProgress;

    public PlayerQuests() {
        this.questsProgress = new HashMap<>();
        for (QUESTS quest : QUESTS.values()) {
            questsProgress.put(quest, 0);
        }
    }

    public int getProgress(QUESTS quest) {
        return questsProgress.getOrDefault(quest, 0);
    }

    public void addProgress(QUESTS quest, int amount) {
        int newProgress = questsProgress.getOrDefault(quest, 0) + amount;
        questsProgress.put(quest, newProgress);
    }

    public boolean isQuestCompleted(QUESTS quest) {
        return getProgress(quest) >= quest.getQt();
    }

}
