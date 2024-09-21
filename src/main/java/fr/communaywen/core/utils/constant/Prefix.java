package fr.communaywen.core.utils.constant;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import lombok.Getter;

/**
 * Enum representing various prefixes for messages.
 * Each prefix is associated with a formatted string using custom colors and fonts.
 */
@Feature("beautiful-message")
@Credit("Axeno")
public enum Prefix {

    // Font: https://lingojam.com/MinecraftSmallFont
    // For gradient color: https://www.birdflop.com/resources/rgb/
    // Color format: §x§r§r§g§g§b§b§l

    HOME("§x§0§8§F§B§5§D§l@§x§2§5§F§C§6§7§lʜ§x§4§1§F§D§7§0§lᴏ§x§5§E§F§E§7§A§lᴍ§x§7§A§F§F§8§3§lᴇ"),
    CLAIM("§x§4§5§9§5§F§F§l@§x§4§1§9§1§F§B§lᴄ§x§3§D§8§D§F§7§lʟ§x§3§9§8§A§F§4§lᴀ§x§3§5§8§6§F§0§lɪ§x§3§1§8§2§E§C§lᴍ"),
    CONTEST("§6§lCONTEST!"),
    SPACE("§x§3§3§0§8§F§B§l@§x§3§6§0§D§F§B§lᴀ§x§3§A§1§1§F§C§lѕ§x§3§D§1§6§F§C§lᴛ§x§4§0§1§B§F§D§lʀ§x§4§4§2§0§F§D§lᴏ§x§4§7§2§4§F§D§lɴ§x§4§A§2§9§F§E§lᴏ§x§4§D§2§E§F§E§lᴍ§x§5§1§3§2§F§F§lɪ§x§5§4§3§7§F§F§lᴇ"),

    // Lucky Block's prefixes
    LUCKYBLOCK("§x§F§C§4§5§8§2L§x§F§C§5§C§9§2u§x§F§D§7§4§A§1c§x§F§D§8§B§B§1k§x§F§E§A§2§C§1y§x§F§E§B§9§D§0B§x§F§E§D§1§E§0l§x§F§F§E§8§E§Fo§x§F§F§F§F§F§Fc§x§F§F§F§F§F§Fk"),
    LUCKYBLOCK_BONUS("§x§F§C§4§5§8§2L§x§F§C§5§C§9§2u§x§F§D§7§4§A§1c§x§F§D§8§B§B§1k§x§F§E§A§2§C§1y§x§F§E§B§9§D§0B§x§F§E§D§1§E§0l§x§F§F§E§8§E§Fo§x§F§F§F§F§F§Fc§x§F§F§F§F§F§Fk §8- §aBonus"),
    LUCKYBLOCK_NEUTRAL("§x§F§C§4§5§8§2L§x§F§C§5§C§9§2u§x§F§D§7§4§A§1c§x§F§D§8§B§B§1k§x§F§E§A§2§C§1y§x§F§E§B§9§D§0B§x§F§E§D§1§E§0l§x§F§F§E§8§E§Fo§x§F§F§F§F§F§Fc§x§F§F§F§F§F§Fk §8- §7Neutre"),
    LUCKYBLOCK_MALUS("§x§F§C§4§5§8§2L§x§F§C§5§C§9§2u§x§F§D§7§4§A§1c§x§F§D§8§B§B§1k§x§F§E§A§2§C§1y§x§F§E§B§9§D§0B§x§F§E§D§1§E§0l§x§F§F§E§8§E§Fo§x§F§F§F§F§F§Fc§x§F§F§F§F§F§Fk §8- §cMalus"),

    ;

    @Getter private final String prefix;
    Prefix(String prefix) {
        this.prefix = prefix;
    }


}
