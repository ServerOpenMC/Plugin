package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Helldivers6 extends BaseAdvancement {
    public Helldivers6(@NotNull Advancement parent) {
        super(
                "helldivers/six",
                new AdvancementDisplay(
                        Material.NETHERITE_SWORD,
                        "Helldivers VI",
                        AdvancementFrameType.CHALLENGE,
                        true,
                        false,
                        6F,10,
                        "Pour la démocratie!!!!"
                ),
                parent
        );
    }
}
