package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.jetbrains.annotations.NotNull;

public class CookedOnionAdvDream extends BaseAdvancement {
    public CookedOnionAdvDream(@NotNull Advancement parent) {
        super(
                "cooked_poissonion",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:cooked_poissonion").getItemStack(),
                        "Poissonion cuit",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3F,11.5F,
                        "Imaginez dans un kebab "+ FontImageWrapper.replaceFontImages(":inlove1:")
                ),
                parent
        );
    }
}
