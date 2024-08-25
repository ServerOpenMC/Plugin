package fr.communaywen.core.luckyblocks.enums;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;
import org.bukkit.Sound;

import java.util.Random;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
@Getter
public enum EventType {

    BONUS("Bonus", new Sound[] {
            Sound.BLOCK_AMETHYST_BLOCK_BREAK,
            Sound.ENTITY_FIREWORK_ROCKET_BLAST,
            Sound.ENTITY_FIREWORK_ROCKET_TWINKLE,
            Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
            Sound.ENTITY_PLAYER_LEVELUP,
            Sound.ENTITY_ILLUSIONER_CAST_SPELL
    }),
    NEUTRAL("Neutre", new Sound[] {
            Sound.ENTITY_VILLAGER_TRADE
    }),
    MALUS("Malus", new Sound[] {
            Sound.ENTITY_TNT_PRIMED,
            Sound.ENTITY_WITHER_AMBIENT,
            Sound.ENTITY_WITCH_CELEBRATE,
            Sound.BLOCK_NOTE_BLOCK_IMITATE_CREEPER,
            Sound.AMBIENT_CAVE,
            Sound.ENTITY_GHAST_HURT,
            Sound.ENTITY_ELDER_GUARDIAN_CURSE,
            Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR
    });

    private final String name;
    private final Sound[] values;

    private final Random random = new Random();

    EventType(String name, Sound[] values) {
        this.name = name;
        this.values = values;
    }

    /**
     * Permet de récupérer un son aléatoire parmi la liste des sons de l'événement en fonction de son type
     * @param eventType le type de l'événement
     * @return un son aléatoire
     */
    public static Sound getRandomSoundByType(EventType eventType) {
        return eventType.getValues()[eventType.getRandom().nextInt(eventType.getValues().length)];
    }
}
