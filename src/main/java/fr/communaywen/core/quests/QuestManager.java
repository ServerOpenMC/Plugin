package fr.communaywen.core.quests;

import java.util.HashMap;
import java.util.Map;

public class QuestManager {
    private Map<String, Quest> quests = new HashMap<>();
    private Map<String, String> playerQuests = new HashMap<>();

    public void addQuest(Quest quest) {
        quests.put(quest.getName(), quest);
    }

    public Quest getQuest(String name) {
        return quests.get(name);
    }

    public Map<String, Quest> getAllQuests() {
        return quests;
    }

    public void assignQuest(String player, String questName) {
        playerQuests.put(player, questName);
    }

    public String getPlayerQuest(String player) {
        return playerQuests.get(player);
    }
}
