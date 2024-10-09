package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.guideline.advancements.dream.DreamTabManager;
import org.bukkit.Material;

public class FishingAdvDream extends BaseAdvancement implements VanillaVisibility {
    public FishingAdvDream() {
        super(
                "fishing",
                new AdvancementDisplay(
                        Material.FISHING_ROD,
                        "Jeter l'ameçon",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        1F,12.5F,
                        "Préparez vous à être étonné"
                ),
                DreamTabManager.getRoot()
        );
    }
}
