package fr.communaywen.core.luckyblocks.managers;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.events.LBGiveBundle;
import fr.communaywen.core.luckyblocks.events.LBMoonGravity;
import fr.communaywen.core.luckyblocks.events.LBSpawnShulker;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;

import java.util.ArrayList;
import java.util.Random;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LuckyBlockManager {

    private final Random random = new Random();
    private final ArrayList<LuckyBlockEvent> lbEvents = new ArrayList<>();

    public LuckyBlockManager() {
        lbEvents.add(new LBGiveBundle());
        lbEvents.add(new LBSpawnShulker());
        lbEvents.add(new LBMoonGravity());
    }

    /**
     * Permet de récupérer un événement aléatoire parmi la liste "lbEvents"
     * @return un événement aléatoire
     */
    public LuckyBlockEvent getRandomEvent() {
        return lbEvents.get(random.nextInt(lbEvents.size()));
    }
}
