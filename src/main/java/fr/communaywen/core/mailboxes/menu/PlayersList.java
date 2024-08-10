package fr.communaywen.core.mailboxes.menu;

import fr.communaywen.core.mailboxes.utils.PaginatedMailbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.getPlayerHead;

public class PlayersList extends PaginatedMailbox<ItemStack> {
    public PlayersList(Player player) {
        super(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            pageItems.add(getPlayerHead(onlinePlayer));
        }
        initInventory();
    }

    @Override
    public void openInventory() {
        player.openInventory(this.inventory);
    }
}
