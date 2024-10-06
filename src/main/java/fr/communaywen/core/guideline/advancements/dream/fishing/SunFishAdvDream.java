package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class SunFishAdvDream extends BaseAdvancement {
    public SunFishAdvDream(@NotNull Advancement parent) {
        super(
                "sunfish",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:sun_fish").getItemStack(),
                        "Poisson-soleil",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3F,13.5F,
                        "Pas sûre que ça sois realiste"
                ),
                parent
        );
    }
}
