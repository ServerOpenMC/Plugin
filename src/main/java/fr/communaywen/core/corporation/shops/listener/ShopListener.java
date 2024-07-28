package fr.communaywen.core.corporation.shops.listener;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.shops.PlayerShopManager;
import fr.communaywen.core.corporation.shops.Shop;
import fr.communaywen.core.corporation.shops.menu.ShopMenu;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.UUID;

public class ShopListener implements Listener {

    private final GuildManager guildManager;
    private final PlayerShopManager playerShopManager;

    public ShopListener(GuildManager guildManager, PlayerShopManager playerShopManager) {
        this.guildManager = guildManager;
        this.playerShopManager = playerShopManager;
    }

    //TODO ItemsAdder caisse

    @EventHandler
    public void onShopBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {
            if (sign.getPersistentDataContainer().has(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
            if (sign.getPersistentDataContainer().has(AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
        }
        if (event.getBlock().getState() instanceof Barrel barrel) {
            if (barrel.getPersistentDataContainer().has(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
            if (barrel.getPersistentDataContainer().has(AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onShopClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getState() instanceof Sign sign) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }
            Shop shop;
            if (sign.getPersistentDataContainer().has(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)) {
                UUID shopUUID = UUID.fromString(Objects.requireNonNull(sign.getPersistentDataContainer().get(AywenCraftPlugin.GUILD_SHOP_KEY, PersistentDataType.STRING)));
                shop = guildManager.getGuild(event.getPlayer().getUniqueId()).getShop(shopUUID);
            }
            else if (sign.getPersistentDataContainer().has(AywenCraftPlugin.PLAYER_SHOP_KEY, PersistentDataType.STRING)) {
                shop = playerShopManager.getShop(event.getPlayer().getUniqueId());
            }
            else {
                return;
            }
            event.setCancelled(true);
            ShopMenu menu = new ShopMenu(event.getPlayer(), guildManager, playerShopManager, shop, 0);
            menu.open();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();

        if (topInventory.getType() == InventoryType.BARREL) {
            if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
                if (!event.isShiftClick() || !event.isLeftClick()) {
                    return;
                }
                ItemStack cursorItem = event.getCursor();

                if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                    ItemMeta itemMeta = cursorItem.getItemMeta();
                    if (itemMeta == null) {
                        return;
                    }
                    itemMeta.getPersistentDataContainer().set(AywenCraftPlugin.SUPPLIER_KEY, PersistentDataType.STRING, event.getWhoClicked().getUniqueId().toString());
                    cursorItem.setItemMeta(itemMeta);
                    event.setCursor(cursorItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();

        if (topInventory.getType() == InventoryType.BARREL) {
            for (int slot : event.getRawSlots()) {
                if (slot < topInventory.getSize()) {
                    ItemStack draggedItem = event.getOldCursor();

                    if (draggedItem.getType() != Material.AIR) {
                        ItemMeta itemMeta = draggedItem.getItemMeta();
                        if (itemMeta == null) {
                            return;
                        }
                        itemMeta.getPersistentDataContainer().set(AywenCraftPlugin.SUPPLIER_KEY, PersistentDataType.STRING, event.getWhoClicked().getUniqueId().toString());
                        draggedItem.setItemMeta(itemMeta);

                        event.setCursor(draggedItem);
                        break;
                    }
                }
            }
        }
    }
}
