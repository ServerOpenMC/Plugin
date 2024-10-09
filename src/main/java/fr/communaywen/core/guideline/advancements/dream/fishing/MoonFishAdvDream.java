package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoonFishAdvDream extends BaseAdvancement implements VanillaVisibility {
    public MoonFishAdvDream(@NotNull Advancement parent) {
        super(
                "moonfish",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:moon_fish").getItemStack(),
                        "Poisson-lune",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,13.5F,
                        "Il porte bien son nom"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 500, "Advancement "+this.display.getTitle());
    }
}
