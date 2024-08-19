package fr.communaywen.core.dreamdim.fishing;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LootStack {
    /**
     * @param LureLevel Lure Level of the fishing tool (0 if the tool doesn't have lure)
     */
    @NotNull
    public abstract Double getChance(int LureLevel);

    @NotNull
    public abstract String getName();

    public abstract @NotNull ItemStack toItemStack(@NotNull Player player);

    /**
     * @param player Player who catch
     */
    public abstract void onCatched(@NotNull Player player);
}
