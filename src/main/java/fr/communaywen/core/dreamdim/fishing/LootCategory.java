package fr.communaywen.core.dreamdim.fishing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class LootCategory {
    @NotNull
    public abstract Set<LootStack> getLoots();

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract Double getChance(int luckLevel);

    public final boolean addToLoot(LootStack lootStack) {
        return getLoots().add(lootStack);
    }

    public final boolean removeFromLoot(LootStack lootStack) {
        return getLoots().remove(lootStack);
    }

    public LootStack pickOne() {
        //ChatGPT :alien:
        double totalChance = 0.0;
        for (LootStack stack : getLoots()) {
            totalChance += stack.getChance();
        }

        if (totalChance != 100) {
            throw new IllegalArgumentException("LootCategory " + getName() + " has chance " + totalChance);
        }

        Random random = new Random();
        double randomValue = random.nextDouble()*100;

        double cumulativeChance = 0.0;
        for (LootStack entry : getLoots()) {
            cumulativeChance += entry.getChance();
            if (randomValue <= cumulativeChance) {
                return entry;
            }
        }

        // This return statement should never be reached, but is required for compilation.
        throw new IllegalStateException("Loot picking failed");
    }

    // Method to pick 'x' loots
    public List<LootStack> pick(int x, Integer LuckLevel) {
        List<LootStack> pickedLoots = new ArrayList<>();

        for (int i = 0; i < x; i++) {
            pickedLoots.add(pickOne());
        }

        return pickedLoots;
    }
}
