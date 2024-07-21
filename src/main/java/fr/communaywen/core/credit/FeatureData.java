package fr.communaywen.core.credit;

import lombok.Getter;

@Getter
public class FeatureData {

    private final String feature;
    private final String[] developers;
    private String[] collaborators;

    public FeatureData(String feature, String[] developers) {
        this.feature = feature;
        this.developers = developers;
    }

    public FeatureData(String feature, String[] developers, String[] collaborators) {
        this.feature = feature;
        this.developers = developers;
        this.collaborators = collaborators;
    }

}
