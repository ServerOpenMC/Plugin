package fr.communaywen.core.dreamdim.fishing.loot_table.fish;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.DreamUtils;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class Poissonion extends LootStack {
    ItemStack fish = CustomStack.getInstance("aywen:poissonion").getItemStack();

    public Poissonion() {
        DreamUtils.setFromDream(fish);
    }

    @Override
    public @NotNull Double getChance() {
        return 33.0;
    }

    @Override
    public @NotNull String getName() {
        return "Poissonion";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        return fish;
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {
        GuidelineManager.getAPI().getAdvancement("dream:poissonion").grant(player);
    }
}
