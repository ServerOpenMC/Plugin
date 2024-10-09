package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.IVisibility;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BookAdvDream extends BaseAdvancement implements VanillaVisibility {
    public BookAdvDream(@NotNull Advancement parent) {
        super(
                "codex",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:codex_somnii").getItemStack(),
                        "Puit de Connaissance",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        2F,8.5F,
                        "Il t'en apprendra beaucoup"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 500, "Advancement "+this.display.getTitle());
    }

    @Override
    public boolean isVisible(@NotNull Player player) {
        return true;
    }
}
