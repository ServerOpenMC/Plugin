package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Helldivers2 extends BaseAdvancement implements VanillaVisibility {
    public Helldivers2(@NotNull Advancement parent) {
        super(
                "helldivers/2",
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

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 10, "Advancement "+this.display.getTitle());
    }
}
