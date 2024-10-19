package fr.communaywen.core.luckyblocks.managers;

import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.luckyblocks.events.bonus.*;
import fr.communaywen.core.luckyblocks.events.malus.*;
import fr.communaywen.core.luckyblocks.events.neutrals.LBBatman;
import fr.communaywen.core.luckyblocks.events.neutrals.LBLeakContest;
import fr.communaywen.core.luckyblocks.events.neutrals.LBSpawnGlowsquid;
import fr.communaywen.core.luckyblocks.events.neutrals.LBStructureHerobrine;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LuckyBlockManager {

    private final Random random = new Random();
    private double sum;

    @Getter
    private final ArrayList<LuckyBlockEvent> lbEvents = new ArrayList<>();

    public LuckyBlockManager(ContestManager contestManager) {
        lbEvents.add(new LBLeakContest(contestManager));
        lbEvents.add(new LBSpawnShulker());
        lbEvents.add(new LBMoonGravity());
        lbEvents.add(new LBSolarGravity());
        lbEvents.add(new LBStructureHerobrine());
        lbEvents.add(new LBSpawnBreeze());
        lbEvents.add(new LBSpawnIllusioner());
        lbEvents.add(new LBSpawnAllay());
        lbEvents.add(new LBSpawnBob());
        lbEvents.add(new LBSpawnGlowsquid());
        lbEvents.add(new LBBatman());
        lbEvents.add(new LBEnderchest());
        lbEvents.add(new LBDarkness());
        lbEvents.add(new LBPoison());
        lbEvents.add(new LBSuperHero());
        lbEvents.add(new LBSuperZero());
        lbEvents.add(new LBDecapitation());

        for (LuckyBlockEvent event : lbEvents) {
            sum += event.getChance();
        }

        initEventsIcon();
    }

    /**
     * Permet de récupérer un événement aléatoire parmi la liste "lbEvents"
     * @return un événement aléatoire
     */
    public LuckyBlockEvent getRandomEvent() {
        List<LuckyBlockEvent> lbEvents = new ArrayList<>(this.lbEvents);
        LuckyBlockEvent eventToReturn;
        Collections.shuffle(lbEvents);

        while (true) {
            double randomNumber = random.nextDouble() * sum;
            double currentPercentage = 0;

            for (LuckyBlockEvent event : lbEvents) {
                currentPercentage += event.getChance();
                if (randomNumber <= currentPercentage) {
                    eventToReturn = event;
                    return eventToReturn;
                }
            }
        }
    }

    public List<String> getLuckyBlocksIds() {
        List<String> names = new ArrayList<>();
        for (LuckyBlockEvent event : lbEvents) {
            names.add(event.getId());
        }
        return names;
    }

    @Nullable
    public LuckyBlockEvent getEventById(String id) {
        for (LuckyBlockEvent event : lbEvents) {
            if (event.getId().equalsIgnoreCase(id)) {
                return event;
            }
        }
        return null;
    }

    private void initEventsIcon() {
        for (LuckyBlockEvent event : lbEvents) {

            ItemStack iconItem = event.getIconItem();
            ItemMeta itemMeta = iconItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();

            itemMeta.setDisplayName("§6" + event.getName());
            lore.add("§bId: §a" + event.getId());
            lore.add("§bChance: §a" + Math.round(event.getChance() * 100) + "%");
            lore.add("§bType: " + event.getEventType().getColor() + event.getEventType().getName());
            lore.add("§bDescription: §f" + event.getDescription());
            itemMeta.setLore(lore);

            iconItem.setItemMeta(itemMeta);
        }
    }
}
