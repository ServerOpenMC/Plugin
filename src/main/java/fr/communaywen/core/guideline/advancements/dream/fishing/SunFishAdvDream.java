package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SunFishAdvDream extends BaseAdvancement implements ParentGrantedVisibility {
    public SunFishAdvDream(@NotNull Advancement parent) {
        super(
                "sunfish",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:sun_fish").getItemStack(),
                        "Poisson-soleil",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3F,13.5F,
                        "Pas sûre que ça sois realiste"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 500, "Advancement "+this.display.getTitle());
    }
}
