package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class MoonFishAdvDream extends BaseAdvancement {
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
}
