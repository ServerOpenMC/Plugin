package fr.communaywen.core.luckyblocks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class LuckyBlockListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        player.getInventory().clear();

        ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte)3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner("luck");
        skull.setItemMeta(meta);

        player.getInventory().addItem(skull);

        player.updateInventory();



    }
}
