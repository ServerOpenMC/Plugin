package fr.communaywen.core.customitems.enums;

import lombok.Getter;

@Getter
public enum ConfigNames {

    BUILDER_WAND_RADIUS("builder-wand-radius");

    private final String name;

    ConfigNames(String name) {
        this.name = name;
    }
}
