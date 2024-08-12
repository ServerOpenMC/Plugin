package fr.communaywen.core.mailboxes.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.*;

public abstract class PaginatedMailbox<T extends ItemStack> extends MailboxInv {
    protected final int maxIndex;
    protected final String invName;
    protected final ArrayList<T> pageItems = new ArrayList<>();
    protected int page = 0;

    public PaginatedMailbox(Player player, int maxIndex, String invName) {
        super(player);
        this.maxIndex = maxIndex;
        this.invName = invName;
    }

    public PaginatedMailbox(Player player, String invName) {
        this(player, 45, invName);
    }

    public PaginatedMailbox(Player player) {
        this(player, "\uF990\uE002");
    }

    protected void initInventory() {
        inventory = Bukkit.createInventory(this, 54, MailboxMenuManager.getInvTitle(invName));
        inventory.setItem(45, homeBtn());
        inventory.setItem(48, previousPageBtn());
        inventory.setItem(49, cancelBtn());
        inventory.setItem(50, nextPageBtn());
        updateInventory(false);
    }

    public T getByIndex(int index) {
        int currentIndex = page * maxIndex + index;
        return currentIndex >= pageItems.size() ? null : pageItems.get(currentIndex);
    }

    private void clearFrom(int index) {
        for (int i = index; i < maxIndex; i++) inventory.clear(i);
    }

    public void nextPage() {
        updatePage(++page);
    }

    public void previousPage() {
        updatePage(--page);
    }

    private void updatePage(int page) {
        int maxPage = pageItems.size() / maxIndex;
        this.page = Math.max(0, Math.min(page, maxPage));
        updateInventory(true);
    }

    protected void updateInventory(boolean clear, int index) {
        for (int i = index; i < maxIndex; i++) {
            ItemStack item = getByIndex(i);
            if (item == null) {
                if (i == 0 && page > 0) {
                    previousPage();
                    return;
                } else if (clear) clearFrom(i);
                break;
            }
            inventory.setItem(i, item);
        }
    }

    protected void updateInventory(boolean clear) {
        updateInventory(clear, 0);
    }
}
