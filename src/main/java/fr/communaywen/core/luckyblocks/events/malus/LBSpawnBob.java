package fr.communaywen.core.luckyblocks.events.malus;

import fr.communaywen.core.luckyblocks.enums.EventType;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockListeners;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LBSpawnBob extends LuckyBlockEvent implements LuckyBlockListeners {

    public LBSpawnBob() {
        super(
                "spawn_bob",
                "Jaloux ?",
                "§l§4Bob§7 est apparu !",
                0.2f,
                EventType.MALUS,
                new ItemStack(Material.ZOMBIE_HEAD)
        );
    }

    @Override
    public void onOpen(Player player, Block block) {
        super.onOpen(player, block);

        LBUtils.spawnBob(block.getLocation());
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) {
            return;
        }

        if (zombie.customName() == null) {
            return;
        }

        if (zombie.getCustomName().isBlank() || !zombie.getCustomName().equals(LBUtils.BOB_NAME)) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(event.getDroppedExp() * 10);

        if (event.getEntity().getKiller() == null) {
            return;
        }

        ItemStack reward = new ItemStack(Material.ZOMBIE_HEAD);
        ItemMeta meta = reward.getItemMeta();
        String killerName = event.getEntity().getKiller().getName();

        meta.setDisplayName("§4§lBob's Head");
        meta.setLore(List.of("§7Tête de §4Bob§7 tué par §6" + killerName));
        reward.setItemMeta(meta);

        event.getDrops().add(reward);
    }
}
