package fr.communaywen.core.guideline.advancements.spawn.head;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class AllHeadFoundAdv extends BaseAdvancement implements VanillaVisibility {

    public AllHeadFoundAdv(@NotNull Advancement parent) {
        super(
                "spawn/head/all",
                new AdvancementDisplay(
                        Material.CREEPER_HEAD,
                        "Pro des Têtes",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3F,2F,
                        "Vous avez trouvé toutes les têtes"
                ),
                parent
        );
    }
}
