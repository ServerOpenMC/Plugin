package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class TotemAdvDream extends BaseAdvancement implements VanillaVisibility {
    public TotemAdvDream(@NotNull Advancement parent) {
        super(
                "totem",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:totem_of_undreaming").getItemStack(),
                        "Tromper le cauchemar",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,6.5F,
                        "Vous avez utilisé un totem de réveil pour vous réveiller"
                ),
                parent
        );
    }
}
