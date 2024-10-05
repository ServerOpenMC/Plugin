package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.Material;

public class FirstDream extends BaseAdvancement {
    public FirstDream() {
        super(
                "firstdream",
                new AdvancementDisplay(
                        Material.SCULK,
                        "Votre premier rêve",
                        AdvancementFrameType.GOAL,
                        false,
                        false,
                        1,0
                        ),
                GuidelineManager.getRoot()
        );
        AywenCraftPlugin.getInstance().registerEvents(
                new fr.communaywen.core.guideline.listeners.dream.FirstDream()
        );
    }
}
