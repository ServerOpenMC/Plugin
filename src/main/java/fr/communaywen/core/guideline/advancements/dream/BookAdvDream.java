package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import dev.lone.itemsadder.api.CustomStack;
import org.jetbrains.annotations.NotNull;

public class BookAdvDream extends BaseAdvancement {
    public BookAdvDream(@NotNull Advancement parent) {
        super(
                "codex",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:codex_somnii").getItemStack(),
                        "Puit de Connaissance",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,8.5F,
                        "Il t'en apprendra beaucoup"
                ),
                parent
        );
    }
}
