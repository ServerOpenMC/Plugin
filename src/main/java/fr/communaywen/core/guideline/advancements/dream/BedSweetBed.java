package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BedSweetBed extends BaseAdvancement {
    public BedSweetBed(@NotNull Advancement parent) {
        super(
                "bedsweetbed",
                new AdvancementDisplay(
                        Material.SCULK,
                        "Votre premier rêve",
                        AdvancementFrameType.TASK,
                        true,
                        false,
                        -2.25F,0,
                        "Vous avez fini votre premier rêve sans encombre"
                ),
                parent
        );

        AywenCraftPlugin.getInstance().registerEvents(
                new fr.communaywen.core.guideline.listeners.dream.BedSweetBed()
        );
    }
}
