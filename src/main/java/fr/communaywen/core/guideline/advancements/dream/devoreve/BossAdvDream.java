package fr.communaywen.core.guideline.advancements.dream.devoreve;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BossAdvDream extends BaseAdvancement implements VanillaVisibility {

    public BossAdvDream(@NotNull Advancement parent) {
        super(
                "dreameater",
                new AdvancementDisplay(
                        Material.WITHER_SKELETON_SKULL,
                        "Terraseur de rêve",
                        AdvancementFrameType.CHALLENGE,
                        true,
                        false,
                        2.5F,7.5F,
                        "Vous avez tuez le dévorêve"
                ),
                parent
        );
    }
}
