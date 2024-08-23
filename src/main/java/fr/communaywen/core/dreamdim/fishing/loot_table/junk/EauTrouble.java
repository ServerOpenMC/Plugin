package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EauTrouble extends LootStack {
    @Override
    public @NotNull Double getChance() {
        return 25.0;
    }

    @Override
    public @NotNull String getName() {
        return "Eau trouble";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        ItemStack potion = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = potion.getItemMeta();
        PotionMeta potmeta = (PotionMeta) meta;

        potmeta.setBasePotionType(PotionType.WATER);
        potmeta.setColor(Color.fromRGB(5965448));
        potion.setItemMeta(potmeta);

        potion.setDisplayName("§rEau trouble");

        return potion;
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {

    }
}
