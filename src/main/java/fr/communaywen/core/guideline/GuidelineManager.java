package fr.communaywen.core.guideline;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.advancements.dream.*;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public class GuidelineManager implements Listener {
    AywenCraftPlugin plugin;

    @Getter static AdvancementTab tab;
    @Getter static GuidelineManager instance;
    @Getter static UltimateAdvancementAPI API;
    @Getter static RootAdvancement root;

    public GuidelineManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        API = UltimateAdvancementAPI.getInstance(plugin);
        tab = API.createAdvancementTab("openmc");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(
                Material.PINK_CONCRETE,
                "OpenMC",
                AdvancementFrameType.CHALLENGE,
                false,
                false,
                100,
                100,
                "Votre aventure commence ici");

        root = new RootAdvancement(tab, "root", rootDisplay, "textures/block/pink_concrete_powder.png");

        DreamTabManager.init();

        tab.registerAdvancements(root);
        tab.automaticallyGrantRootAdvancement();
    }
}
