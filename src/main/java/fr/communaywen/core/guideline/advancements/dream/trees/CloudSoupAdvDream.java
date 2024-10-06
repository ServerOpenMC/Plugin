package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class CloudSoupAdvDream extends BaseAdvancement {
    public CloudSoupAdvDream(@NotNull Advancement parent) {
        super(
                "cloudsoup",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:cloud_soup").getItemStack(),
                        "Voler",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        3F,4.5F,
                        "Vous volez dans les airs comme un nuage"
                ),
                parent
        );
    }
}
