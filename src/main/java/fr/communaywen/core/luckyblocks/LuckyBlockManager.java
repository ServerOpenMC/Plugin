package fr.communaywen.core.luckyblocks;

import java.util.ArrayList;
import java.util.Random;

public class LuckyBlockManager {

    private final Random random = new Random();
    ArrayList<LuckyBlocksEvents> eventList = new ArrayList<>();

    public LuckyBlockManager() {
        eventList.add(new SpawnSkeletonHorse());
        eventList.add(new SpawnZombieHorse());
        eventList.add(new Explosion());
        eventList.add(new Lightning());
        eventList.add(new SpawnBreeze());
        eventList.add(new SpawnIllusionner());
        eventList.add(new SpawnAllay());
        eventList.add(new SpawnZombieNetherite());
        eventList.add(new SpawnGlowSquid());
        eventList.add(new SpawnRideBat());
        eventList.add(new GiveEnderChests());
    }

    public LuckyBlocksEvents getRandomLB() {
        return eventList.get(random.nextInt(eventList.size()));
    }
}
