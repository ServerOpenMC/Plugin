package fr.communaywen.core.dreamdim.fishing.loot_table.fish;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Poissonion extends LootStack {
    CustomStack fish = CustomStack.getInstance("aywen:poissonion");

    @Override
    public @NotNull Double getChance(int LureLevel) {
        return 0.5;
    }

    @Override
    public @NotNull String getName() {
        return "Poissonion";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        return null;
    }

    @Override
    public void onCatched(@NotNull Player player) {

    }
}
