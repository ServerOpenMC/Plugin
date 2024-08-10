package fr.communaywen.core.mailboxes.letter;

import fr.communaywen.core.mailboxes.menu.letter.Letter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.getPlayerName;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.nonItalic;
import static fr.communaywen.core.utils.StringDateFormatter.formatRelativeDate;

public class LetterHead extends ItemStack {
    private final int id;
    private final int itemsCount;
    private final ItemStack[] items;

    public LetterHead(OfflinePlayer player, int id, int itemsCount, LocalDateTime sentAt, ItemStack[] items) {
        super(Material.PLAYER_HEAD, 1);
        this.id = id;
        this.itemsCount = itemsCount;
        this.items = items;
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.displayName(getPlayerName(player));
        ArrayList<Component> lore = new ArrayList<>();
        Component firstLine = Component.text(formatRelativeDate(sentAt), NamedTextColor.DARK_GRAY);
        Component secondLine = Component.text("➤ Contient ", NamedTextColor.DARK_GREEN)
                                        .append(Component.text(itemsCount, NamedTextColor.GREEN, TextDecoration.BOLD))
                                        .append(Component.text(" item" + (itemsCount > 1 ? "s" : ""), NamedTextColor.DARK_GREEN));
        lore.add(nonItalic(firstLine));
        lore.add(nonItalic(secondLine));
        skullMeta.lore(lore);
        this.setItemMeta(skullMeta);
    }

    public LetterHead(OfflinePlayer player, int id, int itemsCount, LocalDateTime sentAt) {
        this(player, id, itemsCount, sentAt, null);
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void openLetter(Player player) {
        Letter letter = new Letter(player, this);
        letter.openInventory();
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public int getId() {
        return id;
    }
}
