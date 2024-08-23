package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.AdvancementRegister;
import fr.communaywen.core.dreamdim.DreamUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BlockPlacement implements Listener {
    AdvancementRegister register;

    public BlockPlacement(AdvancementRegister register) {
        this.register = register;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("dreamworld")) { return; }
        if (List.of(Material.ANCIENT_DEBRIS, Material.ENDER_CHEST, Material.SHULKER_BOX).contains(block.getType())) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("dreamworld")){ return; }
        if (!block.getType().equals(Material.ANCIENT_DEBRIS)) { return; }

        event.setDropItems(false);

        ItemStack essence = CustomStack.getInstance("aywen:dream_essence").getItemStack();
        DreamUtils.setFromDream(essence);

        player.getWorld().dropItemNaturally(block.getLocation(), essence);

        register.grantAdvancement(player, "aywen:dreamrush");
    }
}
