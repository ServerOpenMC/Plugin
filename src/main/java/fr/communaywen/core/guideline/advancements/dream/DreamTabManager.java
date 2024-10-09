package fr.communaywen.core.guideline.advancements.dream;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;

import fr.communaywen.core.guideline.advancements.dream.devoreve.*;
import fr.communaywen.core.guideline.advancements.dream.fishing.*;
import fr.communaywen.core.guideline.advancements.dream.helldivers.*;
import fr.communaywen.core.guideline.advancements.dream.trees.*;

import fr.communaywen.core.guideline.listeners.dream.*;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DreamTabManager implements Listener {
    @Getter static RootAdvancement root;
    static HelldiversManager helldiversManager;

    public static void init() {
        UltimateAdvancementAPI api = GuidelineManager.getAPI();
        AdvancementTab tab = api.createAdvancementTab("dream");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(
                Material.SCULK,
                "Rêves",
                AdvancementFrameType.CHALLENGE,
                true,
                false,
                0F,
                10F,
                "Vos rêves vous guideront");

        root = new RootAdvancement(tab, "root", rootDisplay, "textures/block/sculk.png");

        BaseAdvancement hd1 = new Helldivers1(root);
        BaseAdvancement hd2 = new Helldivers2(hd1);
        BaseAdvancement hd3 = new Helldivers3(hd2);
        BaseAdvancement hd4 = new Helldivers4(hd3);
        BaseAdvancement hd5 = new Helldivers5(hd4);
        BaseAdvancement hd6 = new Helldivers6(hd5);

        BaseAdvancement fish = new FishingAdvDream();
        BaseAdvancement docker = new DockerAdvDream(fish);
        BaseAdvancement poissonion = new OnionAdvDream(fish);
        BaseAdvancement cookedpoissonion = new CookedOnionAdvDream(poissonion);
        BaseAdvancement moonfish = new MoonFishAdvDream(fish);
        BaseAdvancement sunfish = new SunFishAdvDream(moonfish);

        BaseAdvancement essence = new EssenceAdvDream();
        BaseAdvancement totem = new TotemAdvDream(essence);
        BaseAdvancement book = new BookAdvDream(essence);

        BaseAdvancement boss = new BossAdvDream(essence);
        BaseAdvancement bossSkull = new BossSkullAdvDream(boss);
        BaseAdvancement bossChestplate = new BossChestplateAdvDream(boss);
        BaseAdvancement bossWeapon = new BossWeaponAdvDream(boss);

        BaseAdvancement wood = new WoodAdvDream(root);
        BaseAdvancement cloud = new CloudAdvDream(root);
        BaseAdvancement bed = new BedAdvDream(wood,cloud);
        BaseAdvancement cloudSoup = new CloudSoupAdvDream(cloud);
        BaseAdvancement planks = new PlankAdvDream(wood);

        helldiversManager = new HelldiversManager();

        tab.registerAdvancements(root,
                hd1,hd2,hd3,hd4,hd5,hd6, // HellDivers
                fish,docker,poissonion,cookedpoissonion,moonfish,sunfish, // Fishing
                essence,totem,book, // Essence
                boss,bossSkull,bossChestplate,bossWeapon, // Boss
                wood,planks,cloud,bed,cloudSoup // Trees
        );

        tab.automaticallyShowToPlayers();

        AywenCraftPlugin.getInstance().registerEvents(
                new FishingDream(),
                new CookedDreamFish(),
                new TreeBreakAdvancement(),
                new TreeCraftAdvancements(),
                helldiversManager
        );
    }

    public static void close() {
        helldiversManager.close();
    }
}
