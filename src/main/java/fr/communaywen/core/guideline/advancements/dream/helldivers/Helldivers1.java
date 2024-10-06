package fr.communaywen.core.guideline.advancements.dream.helldivers;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Helldivers1 extends BaseAdvancement {
    public Helldivers1(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent) {
        super(
                "helldivers/one",
                new AdvancementDisplay(
                        Material.WOODEN_SWORD,
                        "Helldivers I",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        -2.25F,1,
                        "Pour la démocratie"
                ),
                GuidelineManager.getRoot()
        );
        AywenCraftPlugin.getInstance().registerEvents(
                new fr.communaywen.core.guideline.listeners.dream.FirstDream()
        );
    }
}
