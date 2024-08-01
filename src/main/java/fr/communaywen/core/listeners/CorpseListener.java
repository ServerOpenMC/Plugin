package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.corpse.CorpseMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CorpseListener implements Listener {

    private CorpseManager corpseManager;

    public CorpseListener(CorpseManager corpseManager) {
        this.corpseManager = corpseManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();

        corpseManager.addCorpse(e.getEntity(), e.getEntity().getInventory());
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            corpseManager.open(e.getClickedBlock().getLocation(), e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (corpseManager.isCorpseInventory(e.getClickedInventory())) {
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                e.setCancelled(true);
            } /*else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER) {
                Player p = (Player) e.getWhoClicked();

                Inventory corpseInventory = e.getClickedInventory();
                Inventory playerInventory = p.getInventory();

                CorpseMenu corpseMenu = corpseManager.getCorpseMenuByPlayer(p);

                if(corpseMenu == null){
                    return;
                }

                corpseMenu.swapContents(playerInventory, corpseInventory);

                p.sendMessage("§aVous avez récupéré tout le stuff de la tombe.");
                p.closeInventory();
            }*/
        }
    }

    @EventHandler
    public void onBreak(CustomBlockBreakEvent e) {
        ItemStack block = e.getCustomBlockItem();

        if (block.hasItemMeta() && block.getItemMeta().hasDisplayName() && block.getItemMeta().getDisplayName().equalsIgnoreCase("§fgrave")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (corpseManager.isCorpseInventory(e.getInventory())) {
            corpseManager.close(e.getInventory());
        }
    }

}
