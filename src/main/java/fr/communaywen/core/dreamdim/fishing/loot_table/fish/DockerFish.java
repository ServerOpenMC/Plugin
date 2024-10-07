package fr.communaywen.core.dreamdim.fishing.loot_table.fish;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.fishing.LootStack;
import fr.communaywen.core.guideline.GuidelineManager;
import fr.communaywen.core.utils.ContainerNameGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DockerFish extends LootStack {
    CustomStack fish = CustomStack.getInstance("aywen:dockerfish");

    @Override
    public @NotNull Double getChance() {
        return 34.0;
    }

    @Override
    public @NotNull String getName() {
        return "Poisson Docker";
    }

    @Override
    public @NotNull ItemStack toItemStack(@Nullable Player player) {
        ItemStack item = fish.getItemStack();
        ItemMeta meta = item.getItemMeta();

        meta.lore(List.of(Component.text("§3"+ ContainerNameGenerator.getRandomName(0))));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public void onCatched(@NotNull Player player, @NotNull FishHook fishHook) {
        GuidelineManager.getAPI().getAdvancement("dream:dockerfish").grant(player);
    }
}
