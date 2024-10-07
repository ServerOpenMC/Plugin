package fr.communaywen.core.guideline.advancements.dream.devoreve;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.dreamdim.listeners.BossManager;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BossSkullAdvDream extends BaseAdvancement implements VanillaVisibility {

    public BossSkullAdvDream(@NotNull Advancement parent) {
        super(
                "devoreve/skull",
                new AdvancementDisplay(
                        BossManager.getHelmet(),
                        "Tête du Dévorêve",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3.5F,6.5F,
                        "Vous avez récupérér la tête du Dévorêve"
                ),
                parent
        );
    }
}
