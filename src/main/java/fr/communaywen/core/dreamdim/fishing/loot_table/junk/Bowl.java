package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Bowl extends LootStack {

    @Override
    public @NotNull Double getChance() {
        return 50.0;
    }

    @Override
    public @NotNull String getName() {
        return "Bowl";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        return new ItemStack(Material.BOWL);
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {
        return;
    }
}
