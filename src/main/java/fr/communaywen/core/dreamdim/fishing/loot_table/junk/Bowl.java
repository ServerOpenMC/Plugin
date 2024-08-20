package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Bowl extends LootStack {

    @Override
    public @NotNull Double getChance(int LuckLevel) {
        return 1.0;
    }

    @Override
    public @NotNull String getName() {
        return "Bowl";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        return new ItemStack(Material.BOWL);
    }

    @Override
    public void onCatched(@NotNull Player player) {
        return;
    }
}
