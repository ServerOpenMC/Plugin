package fr.communaywen.core.mailboxes.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.playerInventories;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.sendFailureMessage;

public abstract class MailboxInv extends DatabaseConnector implements InventoryHolder {
    protected static String invErrorMessage;
    protected final Player player;
    protected final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    protected Inventory inventory;

    public MailboxInv(Player player) {
        this.player = player;
    }

    public void addInventory() {
        playerInventories.put(player, this);
    }

    public void removeInventory() {
        playerInventories.remove(player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void openInventory() {
        if (inventory == null) {
            sendInvErrorMessage(player);
            return;
        }
        player.openInventory(this.inventory);
    }

    protected void sendInvErrorMessage(Player player) {
        if (invErrorMessage == null) return;
        sendFailureMessage(player, invErrorMessage);
    }
}
