package fr.communaywen.core.dreamdim.fishing.loot_table.treasures;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cow extends LootStack {
    @Override
    public @NotNull Double getChance() {
        return 100.0;
    }

    @Override
    public @NotNull String getName() {
        return "Vache";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        return new ItemStack(Material.AIR);
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {
        Entity mycow = fishHook.getWorld().spawnEntity(fishHook.getLocation(), EntityType.COW);
        Vector direction = player.getLocation().toVector().subtract(mycow.getLocation().toVector()).normalize();

        mycow.setVelocity(direction.multiply(1.5));
    }
}
