package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class PlankAdvDream extends BaseAdvancement implements VanillaVisibility {
    public PlankAdvDream(@NotNull Advancement parent) {
        super(
                "planks",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:dream_planks").getItemStack(),
                        "Bu",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        3F,5F,
                        "Verbe boire au plus-que-parfait"
                ),
                parent
        );
    }
}
