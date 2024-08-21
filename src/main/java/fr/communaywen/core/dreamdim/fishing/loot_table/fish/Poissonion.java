package fr.communaywen.core.dreamdim.fishing.loot_table.fish;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.DreamUtils;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Poissonion extends LootStack {
    ItemStack fish = CustomStack.getInstance("aywen:poissonion").getItemStack();

    public Poissonion() {
        DreamUtils.setFromDream(fish);
    }

    @Override
    public @NotNull Double getChance() {
        return 0.33;
    }

    @Override
    public @NotNull String getName() {
        return "Poissonion";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        return fish;
    }

    @Override
    public void onCatched(@NotNull Player player) {

    }
}