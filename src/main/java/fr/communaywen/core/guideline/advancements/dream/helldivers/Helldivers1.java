package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Helldivers1 extends BaseAdvancement implements VanillaVisibility {
    public Helldivers1(@NotNull Advancement parent) {
        super(
                "helldivers/1",
                new AdvancementDisplay(
                        Material.WOODEN_SWORD,
                        "Helldivers I",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        1F,10F,
                        "Pour la démocratie"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 1, "Advancement "+this.display.getTitle());
    }
}
