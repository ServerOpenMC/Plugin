package fr.communaywen.core.guideline;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import lombok.Getter;
import org.bukkit.Material;

public class GuidelineManager {
    AywenCraftPlugin plugin;

    @Getter static AdvancementTab tab;
    @Getter static GuidelineManager instance;

    public GuidelineManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        UltimateAdvancementAPI api = UltimateAdvancementAPI.getInstance(plugin);
        tab = api.createAdvancementTab("openmc");

        RootAdvancement root = new RootAdvancement(tab, "root", new AdvancementDisplay(Material.OAK_SAPLING, "Root Advancement", AdvancementFrameType.CHALLENGE, false, false, 0, 0), "textures/block/cobblestone.png");
    }
}
