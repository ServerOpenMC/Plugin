package fr.communaywen.core.guideline.advancements.dream.devoreve;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.dreamdim.listeners.BossManager;
import org.jetbrains.annotations.NotNull;

public class BossChestplateAdvDream extends BaseAdvancement implements VanillaVisibility {

    public BossChestplateAdvDream(@NotNull Advancement parent) {
        super(
                "devoreve/chestplate",
                new AdvancementDisplay(
                        BossManager.getChestplate(),
                        "Plastron du Dévorêve",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3.5F,7.5F,
                        "Vous avez récupérér le plastron du Dévorêve"
                ),
                parent
        );
    }
}
