package fr.communaywen.core.dreamdim.fishing.loot_table.fish;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.DreamUtils;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MoonFish extends LootStack {
    ItemStack moon_fish =CustomStack.getInstance("aywen:moon_fish").getItemStack();

    public MoonFish() {
        DreamUtils.setFromDream(moon_fish);
    }

    @Override
    public @NotNull Double getChance() {
        return 0.5;
    }

    @Override
    public @NotNull String getName() {
        return "Poisson-lune";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        return moon_fish;
    }

    @Override
    public void onCatched(@NotNull Player player) {

    }
}
