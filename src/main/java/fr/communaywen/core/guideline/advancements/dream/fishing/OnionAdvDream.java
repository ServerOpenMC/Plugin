package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class OnionAdvDream extends BaseAdvancement {
    public OnionAdvDream(@NotNull Advancement parent) {
        super(
                "poissonion",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:poissonion").getItemStack(),
                        "Poissonion",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,11.5F,
                        "Un poisson, nan, un onion, eeuh, je sais pas"
                ),
                parent
        );
    }
}
