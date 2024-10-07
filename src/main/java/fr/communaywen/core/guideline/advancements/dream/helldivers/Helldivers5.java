package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Helldivers5 extends BaseAdvancement {
    public Helldivers5(@NotNull Advancement parent) {
        super(
                "helldivers/fifth",
                new AdvancementDisplay(
                        Material.DIAMOND_SWORD,
                        "Helldivers V",
                        AdvancementFrameType.GOAL,
                        true,
                        false,
                        5F,10,
                        "Pour la démocratie!!!"
                ),
                parent
        );
    }

    @Override
    public void giveReward(@NotNull Player player) {
        EconomyManager.getInstance().addBalance(player, 1000, "Advancement "+this.display.getTitle());
    }
}
