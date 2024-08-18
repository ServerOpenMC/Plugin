package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

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

    @EventHandler
    public void onBreak(BlockBreakEvent e){

        Player player = e.getPlayer();
        Block block = e.getBlock();
        BlockState bs = block.getState();
        Location location = e.getBlock().getLocation().add(0.5,0,0.5);


        if (bs instanceof Skull){

            Skull skull = (Skull) bs;
            if(skull.getOwner().equalsIgnoreCase("luck")){
                e.setCancelled(true);
                block.setType(Material.AIR);

                Random random = new Random();
                int alea = random.nextInt(4);

                switch (alea){
                    case 0:
                        location.getWorld().spawn(location, SkeletonHorse.class);
                        break;

                    case 1:
                        location.getWorld().spawn(location, ZombieHorse.class);
                        break;

                    case 2:
                        location.getWorld().createExplosion(location, 2, true);

                }
            }
        }

    }
}
