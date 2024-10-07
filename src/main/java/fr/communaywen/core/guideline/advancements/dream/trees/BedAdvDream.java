package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.multiParents.MultiParentsAdvancement;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedAdvDream extends MultiParentsAdvancement implements HiddenVisibility {
    public BedAdvDream(@NotNull BaseAdvancement... parents) {
        super(
                "bed",
                new AdvancementDisplay(
                        Material.RED_BED,
                        "Dormir sur un nuage",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        2F,4.5F,
                        "Vous dormez sur un nuage"
                ),
                parents
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 1, "Advancement "+this.display.getTitle());
    }
}
