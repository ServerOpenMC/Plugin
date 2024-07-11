package fr.communaywen.core.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class TradeListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Trade trade = Trade.tradesPlayer1.get((Player) e.getWhoClicked());
        if (trade == null) {
            trade = Trade.tradesPlayer2.get((Player) e.getWhoClicked());
            if (trade == null) {
                return;
            }

            if (trade.inventory1 == e.getClickedInventory()) {
                e.setCancelled(true);
            }
        } else {
            if (trade.inventory2 == e.getClickedInventory()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        Trade trade = Trade.tradesPlayer1.get((Player) e.getWhoClicked());
        if (trade == null) {
            trade = Trade.tradesPlayer2.get((Player) e.getWhoClicked());
            if (trade == null) {
                return;
            }

            if (trade.inventory1 == e.getInventory()) {
                e.setCancelled(true);
            }
        } else {
            if (trade.inventory2 == e.getInventory()) {
                e.setCancelled(true);
            }
        }
    }
}
