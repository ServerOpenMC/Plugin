package fr.communaywen.core.guideline.advancements.dream.trees;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CloudSoupAdvDream extends BaseAdvancement implements VanillaVisibility {
    public CloudSoupAdvDream(@NotNull Advancement parent) {
        super(
                "cloudsoup",
                new AdvancementDisplay(
                        CustomStack.getInstance("aywen:cloud_soup").getItemStack(),
                        "Voler",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        3F,4F,
                        "Fly me to the mooon..."
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 500, "Advancement "+this.display.getTitle());
    }
}
