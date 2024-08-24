package fr.communaywen.core.dreamdim.fishing;

import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LootStack {
    @NotNull
    public abstract Double getChance();

    @NotNull
    public abstract String getName();

    public abstract @NotNull ItemStack toItemStack(@Nullable Player player);

    /**
     * @param player Player who catch
     */
    public abstract void onCatched(@NotNull Player player, @NotNull FishHook fishHook);
}
