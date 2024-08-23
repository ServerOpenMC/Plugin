package fr.communaywen.core.dreamdim;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

public class SimpleAdvancementRegister {
    /**
     * @param player Le joueur qui recevra le succès
     * @param namespace Le NameSpace du succès
     * @param criteria Le critère à accordée
     * @return Si le succès a été accordée
     */
    public static boolean grantAdvancement(Player player, String namespace, String criteria) {
        NamespacedKey key = NamespacedKey.fromString(namespace);
        if (key == null) { return false; }

        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) { return false; }

        return player.getAdvancementProgress(advancement).awardCriteria(criteria);
    }

    /**
     * @param player Le joueur qui recevra le succès
     * @param namespace Le NameSpace du succès
     * @return Si le succès a été accordée
     */
    public static boolean grantAdvancement(Player player, String namespace) {
        NamespacedKey key = NamespacedKey.fromString(namespace);
        if (key == null) { return false; }

        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) { return false; }

        return player.getAdvancementProgress(advancement).awardCriteria("requirement");
    }
}
