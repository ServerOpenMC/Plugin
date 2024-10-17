package fr.communaywen.core.guideline.advancements.spawn.head;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class OneHeadFoundAdv extends BaseAdvancement implements VanillaVisibility {

    public OneHeadFoundAdv(@NotNull Advancement parent) {
        super(
                "spawn/head/1",
                new AdvancementDisplay(
                        Material.PLAYER_HEAD,
                        "Novice des Têtes",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,2F,
                        "Vous avez trouvé une tête"
                ),
                parent
        );
    }
}
