package fr.communaywen.core.dreamdim.fishing.loot_table;

import fr.communaywen.core.dreamdim.fishing.LootCategory;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import fr.communaywen.core.dreamdim.fishing.loot_table.treasures.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Treasures extends LootCategory {
    @Override
    public @NotNull Set<LootStack> getLoots() {
        return Set.of(
                new Cow()
        );
    }

    @Override
    public @NotNull String getName() {
        return "Trésors";
    }

    @Override
    public @NotNull Double getChance(int luckLevel) {
        return 10.0;
    }
}
