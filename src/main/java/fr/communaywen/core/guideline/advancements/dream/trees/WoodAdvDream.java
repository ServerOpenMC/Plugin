package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class WoodAdvDream extends BaseAdvancement implements VanillaVisibility {
    public WoodAdvDream(@NotNull Advancement parent) {
        super(
                "wood",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:dream_log").getItemStack(),
                        "Bûcheron de rêve",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        1F,5F,
                        "Vous avez abattu un arbre de rêve"
                ),
                parent
        );
    }
}
