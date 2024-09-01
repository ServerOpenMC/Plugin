package fr.communaywen.core.luckyblocks.managers;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.events.LBMoonGravity;
import fr.communaywen.core.luckyblocks.events.LBSolarGravity;
import fr.communaywen.core.luckyblocks.events.LBSpawnShulker;
import fr.communaywen.core.luckyblocks.events.LBStructureHerobrine;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LuckyBlockManager {

    private final Random random = new Random();
    private double sum;

    @Getter
    private final ArrayList<LuckyBlockEvent> lbEvents = new ArrayList<>();

    public LuckyBlockManager() {
        lbEvents.add(new LBSpawnShulker());
        lbEvents.add(new LBMoonGravity());
        lbEvents.add(new LBSolarGravity());
        lbEvents.add(new LBStructureHerobrine());

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

    private void initEventsIcon() {
        for (LuckyBlockEvent event : lbEvents) {

            ItemStack iconItem = event.getIconItem();
            ItemMeta itemMeta = iconItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();

            itemMeta.setDisplayName("§6" + event.getName());
            lore.add("§bChance: §a" + event.getChance() * 100 + "%");
            lore.add("§bType: " + event.getEventType().getColor() + event.getEventType().getName());
            lore.add("§bDescription: §f" + event.getDescription());
            itemMeta.setLore(lore);

            iconItem.setItemMeta(itemMeta);
        }
    }
}
