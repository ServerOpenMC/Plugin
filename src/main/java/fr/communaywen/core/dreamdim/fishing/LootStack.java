package fr.communaywen.core.dreamdim.fishing;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LootStack {
    @NotNull
    public abstract Double getChance();

    @NotNull
    public abstract String getName();

    public abstract @NotNull ItemStack toItemStack(@NotNull Player player);

    /**
     * @param player Player who catch
     */
    public abstract void onCatched(@NotNull Player player);
}
