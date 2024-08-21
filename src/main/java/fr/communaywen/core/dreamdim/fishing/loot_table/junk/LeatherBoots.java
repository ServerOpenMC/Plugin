package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class LeatherBoots extends LootStack {
    @Override
    public @NotNull Double getChance() {
        return 50.0;
    }

    @Override
    public @NotNull String getName() {
        return "Botte en cuir";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = item.getItemMeta();
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(65-(new Random().nextInt(24-10)+10));

        item.setItemMeta(damageable);

        return item;
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {

    }
}
