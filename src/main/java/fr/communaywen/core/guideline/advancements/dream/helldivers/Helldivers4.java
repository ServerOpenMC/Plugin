package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Helldivers4 extends BaseAdvancement {
    public Helldivers4(@NotNull Advancement parent) {
        super(
                "helldivers/fourth",
                new AdvancementDisplay(
                        Material.GOLDEN_SWORD,
                        "Helldivers IV",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        4F,10,
                        "Pour la démocratie!!"
                ),
                parent
        );
    }
}
