package fr.communaywen.core.guideline.advancements.spawn.jump;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Top10JumpAdv extends BaseAdvancement implements VanillaVisibility {

    public Top10JumpAdv(@NotNull Advancement parent) {
        super(
                "spawn/jump/top10jump",
                new AdvancementDisplay(
                        Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
                        "Un des meilleurs!",
                        AdvancementFrameType.CHALLENGE,
                        true,
                        false,
                        3F,3F,
                        "Vous avez été une fois dans le Top 10!"
                ),
                parent
        );
    }
}
