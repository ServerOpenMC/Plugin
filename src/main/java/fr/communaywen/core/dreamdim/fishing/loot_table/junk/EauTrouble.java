package fr.communaywen.core.dreamdim.fishing.loot_table.junk;

import fr.communaywen.core.dreamdim.fishing.LootStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class EauTrouble extends LootStack {
    @Override
    public @NotNull Double getChance() {
        return 0.25;
    }

    @Override
    public @NotNull String getName() {
        return "Eau trouble";
    }

    @Override
    public @NotNull ItemStack toItemStack(@NotNull Player player) {
        ItemStack potion = new ItemStack(Material.GLASS_BOTTLE);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setBasePotionType(PotionType.WATER);
        meta.setColor(Color.fromRGB(5965448));
        potion.setItemMeta(meta);

        potion.setDisplayName("§rEau trouble");

        return potion;
    }

    @Override
    public void onCatched(@NotNull Player player) {

    }
}
