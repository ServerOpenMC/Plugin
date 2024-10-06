package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Helldivers3 extends BaseAdvancement {
    public Helldivers3(@NotNull Advancement parent) {
        super(
                "helldivers/three",
                new AdvancementDisplay(
                        Material.IRON_SWORD,
                        "Helldivers III",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        3F,10,
                        "Pour la démocratie!!"
                ),
                parent
        );
    }
}
