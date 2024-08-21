package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LeatherBoots extends LootStack {
    @Override
    public @NotNull Double getChance() {
        return 0.25;
    }

    @Override
    public @NotNull String getName() {
        return "Botte en cuir";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        return new ItemStack(Material.LEATHER_BOOTS);
    }

    @Override
    public void onCatched(@NotNull Player player) {

    }
}
