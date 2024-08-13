package fr.communaywen.core.mailboxes.utils;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullOwner {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();

    public static void setSkullOwner(ItemStack skullItem, OfflinePlayer player) {
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        if (player.isOnline()) {
            skullMeta.setOwningPlayer(player);
            skullItem.setItemMeta(skullMeta);
        } else {
            player.getPlayerProfile().update().thenAcceptAsync(updatedProfile -> {
                skullMeta.setPlayerProfile(updatedProfile);
                skullItem.setItemMeta(skullMeta);
            }, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
        }
    }
}
