package fr.communaywen.core.dreamdim.fishing.loot_table;

import fr.communaywen.core.dreamdim.fishing.LootCategory;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import fr.communaywen.core.dreamdim.fishing.loot_table.junk.*;

public class Junk extends LootCategory {

    @Override
    public @NotNull Set<LootStack> getLoots() {
        return Set.of( //Ajoutez en haut pour éviter les conflits, merci
            new Bowl(),
            //new EauTrouble(),
            new LeatherBoots()
        );
    }

    @Override
    public @NotNull String getName() {
        return "Junk";
    }

    @Override
    public @NotNull Double getChance(int luckLevel) {
        return 30.0;
    }
}
