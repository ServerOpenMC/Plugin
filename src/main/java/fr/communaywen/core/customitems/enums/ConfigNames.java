package fr.communaywen.core.customitems.enums;

public enum ConfigNames {

    BUILDER_WAND_RADIUS("builder-wand-radius");

    private final String name;

    ConfigNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
