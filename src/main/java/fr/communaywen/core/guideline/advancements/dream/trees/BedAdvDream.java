package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.multiParents.MultiParentsAdvancement;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BedAdvDream extends MultiParentsAdvancement {
    public BedAdvDream(@NotNull BaseAdvancement... parents) {
        super(
                "bed",
                new AdvancementDisplay(
                        Material.RED_BED,
                        "Dormir sur un nuage",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        2F,5F,
                        "Vous dormez sur un nuage"
                ),
                parents
        );
    }
}
