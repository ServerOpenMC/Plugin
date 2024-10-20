package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Helldivers4 extends BaseAdvancement implements VanillaVisibility {
    public Helldivers4(@NotNull Advancement parent) {
        super(
                "helldivers/4",
                new AdvancementDisplay(
                        Material.GOLDEN_SWORD,
                        "Helldivers IV",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        4F,10,
                        "Pour la démocratie!!"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player.getUniqueId(), 500, "Advancement "+this.display.getTitle());
    }
}
