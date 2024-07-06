package fr.communaywen.core.quests;

public class Quest {
    private String name;
    private String description;
    private int reward;

    public Quest(String name, String description, int reward) {
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getReward() {
        return reward;
    }
}
