package fr.communaywen.core.guideline.advancements.dream.devoreve;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.dreamdim.listeners.BossManager;
import org.jetbrains.annotations.NotNull;

public class BossWeaponAdvDream extends BaseAdvancement implements VanillaVisibility {

    public BossWeaponAdvDream(@NotNull Advancement parent) {
        super(
                "devoreve/weapon",
                new AdvancementDisplay(
                        BossManager.getWeapon(),
                        "Houe du Dévorêve",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3.5F,8.5F,
                        "Vous avez récupérér la houe du Dévorêve"
                ),
                parent
        );
    }
}
