package fr.communaywen.core.guideline.advancements.dream.fishing;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CookedOnionAdvDream extends BaseAdvancement implements ParentGrantedVisibility {
    public CookedOnionAdvDream(@NotNull Advancement parent) {
        super(
                "cooked_poissonion",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:cooked_poissonion").getItemStack(),
                        "Poissonion cuit",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        3F,11.5F,
                        "Imaginez dans un kebab "+ FontImageWrapper.replaceFontImages(":inlove1:")
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 500, "Advancement "+this.display.getTitle());
    }
}
