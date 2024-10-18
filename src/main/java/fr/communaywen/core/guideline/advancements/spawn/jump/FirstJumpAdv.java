package fr.communaywen.core.guideline.advancements.spawn.jump;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class FirstJumpAdv extends BaseAdvancement implements VanillaVisibility {

    public FirstJumpAdv(@NotNull Advancement parent) {
        super(
                "spawn/jump/firstjump",
                new AdvancementDisplay(
                        Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                        "Le Jump du Serveur",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        2F,3F,
                        "Vous avez fini le Jump"
                ),
                parent
        );
    }
}
