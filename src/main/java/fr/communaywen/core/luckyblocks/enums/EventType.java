package fr.communaywen.core.luckyblocks.enums;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Random;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
@Getter
public enum EventType {

    BONUS("Bonus", Prefix.LUCKYBLOCK_BONUS, ChatColor.GREEN, new Sound[] {
            Sound.BLOCK_AMETHYST_BLOCK_BREAK,
            Sound.ENTITY_FIREWORK_ROCKET_BLAST,
            Sound.ENTITY_FIREWORK_ROCKET_TWINKLE,
            Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
            Sound.ENTITY_PLAYER_LEVELUP,
            Sound.ENTITY_ILLUSIONER_CAST_SPELL,
            Sound.ENTITY_VILLAGER_YES,
            Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM,
            Sound.BLOCK_END_PORTAL_SPAWN
    }),
    NEUTRAL("Neutre", Prefix.LUCKYBLOCK_NEUTRAL, ChatColor.GRAY, new Sound[] {
            Sound.BLOCK_NOTE_BLOCK_GUITAR,
            Sound.BLOCK_NOTE_BLOCK_BIT,
            Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED,
            Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED,
            Sound.ENTITY_ZOMBIE_VILLAGER_CURE,
            Sound.WEATHER_RAIN,
            Sound.ENTITY_SPLASH_POTION_BREAK,
            Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,
            Sound.BLOCK_ENCHANTMENT_TABLE_USE,
            Sound.BLOCK_BEACON_DEACTIVATE,
            Sound.BLOCK_STEM_BREAK,
            Sound.BLOCK_DISPENSER_DISPENSE,
            Sound.BLOCK_SAND_BREAK,
            Sound.BLOCK_GLASS_BREAK,
            Sound.BLOCK_ANVIL_DESTROY
    }),
    MALUS("Malus", Prefix.LUCKYBLOCK_MALUS, ChatColor.RED, new Sound[] {
            Sound.ENTITY_TNT_PRIMED,
            Sound.ENTITY_WITHER_AMBIENT,
            Sound.ENTITY_WITCH_CELEBRATE,
            Sound.BLOCK_NOTE_BLOCK_IMITATE_CREEPER,
            Sound.AMBIENT_CAVE,
            Sound.ENTITY_GHAST_HURT,
            Sound.ENTITY_ELDER_GUARDIAN_CURSE,
            Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR,
            Sound.ENTITY_ENDERMAN_STARE
    });

    private final String name;
    private final Prefix prefix;
    private final ChatColor color;
    private final Sound[] values;

    private final Random random = new Random();

    EventType(String name, Prefix prefix, ChatColor color, Sound[] values) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
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
