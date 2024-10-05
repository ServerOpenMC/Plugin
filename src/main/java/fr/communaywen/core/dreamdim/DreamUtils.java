package fr.communaywen.core.dreamdim;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DreamUtils {
    /**
    Déclare qu'un ItemStack provient de la dimension des rêves
     */
    public static void setFromDream(ItemStack itemStack) {
        NamespacedKey fromdream_ns = NamespacedKey.fromString("aywen:fromdream");
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(fromdream_ns, PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);
    }

    public static boolean isBoss(Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        return Boolean.TRUE.equals(dataContainer.get(NamespacedKey.minecraft("dream_boss"), PersistentDataType.BOOLEAN));
    }

    public static boolean isFromDream(ItemStack itemStack) {
        NamespacedKey fromdream_ns = NamespacedKey.fromString("aywen:fromdream");
        return Boolean.TRUE.equals(itemStack.getPersistentDataContainer().get(NamespacedKey.fromString("aywen:fromdream"), PersistentDataType.BOOLEAN));
    }
}
