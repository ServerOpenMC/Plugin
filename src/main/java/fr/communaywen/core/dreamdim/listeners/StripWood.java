package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StripWood implements Listener {
    CustomBlock stripped;

    public StripWood() {
        stripped = CustomBlock.getInstance("aywen:stripped_dream_log");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){ return; }
        Block block = event.getClickedBlock();

        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        if (customBlock == null){ return; }
        if (!customBlock.getNamespacedID().equals("aywen:dream_log")) {return;}
        if (!item.getType().toString().toLowerCase().contains("axe")) {return;}

        stripped.place(block.getLocation());
        player.playSound(block.getLocation(), Sound.ITEM_AXE_STRIP, 0.9F, 1);
        item.damage(1);
    }
}
