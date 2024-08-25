package fr.communaywen.core.dreamdim.fishing.loot_table;

import fr.communaywen.core.dreamdim.fishing.LootCategory;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import fr.communaywen.core.dreamdim.fishing.loot_table.fish.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Fish extends LootCategory {

    @Override
    public @NotNull Set<LootStack> getLoots() {
        return Set.of( //Ajoutez en haut pour éviter les conflits, merci
            new DockerFish(),
            new Poissonion(),
            new MoonFish()
        );
    }

    @Override
    public @NotNull String getName() {
        return "Fish";
    }

    @Override
    public @NotNull Double getChance(int luckLevel) {
        return 60.0;
    }
}
