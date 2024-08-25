package fr.communaywen.core.luckyblocks.managers;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.events.LBExplode;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;

import java.util.ArrayList;
import java.util.Random;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
public class LuckyBlockManager {

    private final Random random = new Random();
    private final ArrayList<LuckyBlockEvent> lbEvents = new ArrayList<>();

    public LuckyBlockManager() {
        lbEvents.add(new LBExplode());
    }

    public LuckyBlockEvent getRandomEvent() {
        return lbEvents.get(random.nextInt(lbEvents.size()));
    }
}
