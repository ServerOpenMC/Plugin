package fr.communaywen.core.mailboxes;

import com.destroystokyo.paper.profile.PlayerProfile;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.concurrent.CompletableFuture;

public class OfflineHead extends ItemStack {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();

    public OfflineHead() {
        super(Material.PLAYER_HEAD, 1);
    }

    public CompletableFuture<Void> setSkullOwner(OfflinePlayer player) {
        if (player.isOnline()) {
            SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
            skullMeta.setOwningPlayer(player);
            this.setItemMeta(skullMeta);
            return null;
        } else {
            PlayerProfile profile = player.getPlayerProfile();
            if (!profile.hasTextures()) {
                profile.update().thenAcceptAsync(this::setProfile, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
                return null;
            } else {
                setProfile(profile);
                return null;
            }
        }
    }

    private void setProfile(PlayerProfile profile) {
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setPlayerProfile(profile);
        this.setItemMeta(skullMeta);
    }
}
