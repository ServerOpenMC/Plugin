package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Helldivers2 extends BaseAdvancement {
    public Helldivers2(@NotNull Advancement parent) {
        super(
                "helldivers/two",
                new AdvancementDisplay(
                        Material.STONE_SWORD,
                        "Helldivers II",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,10,
                        "Pour la démocratie!"
                ),
                parent
        );
    }
}
