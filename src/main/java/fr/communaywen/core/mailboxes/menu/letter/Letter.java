package fr.communaywen.core.mailboxes.menu.letter;

import fr.communaywen.core.mailboxes.letter.LetterHead;
import fr.communaywen.core.mailboxes.utils.MailboxInv;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import fr.communaywen.core.utils.serializer.BukkitSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.*;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.*;

public class Letter extends MailboxInv {
    private final static String INV_NAME = "\uF990\uE003";

    static {
        invErrorMessage = "Erreur lors de la récupération de votre boite aux lettres.";
    }

    private final int id;
    private final int itemsCount;
    private ItemStack[] items;

    public Letter(Player player, LetterHead letterHead) {
        super(player);
        this.id = letterHead.getId();
        this.itemsCount = letterHead.getItemsCount();
        this.items = letterHead.getItems();
        if (items != null || getMailboxById()) {
            inventory = Bukkit.createInventory(this, 54, MailboxMenuManager.getInvTitle(INV_NAME));
            inventory.setItem(45, homeBtn());
            inventory.setItem(48, acceptBtn());
            inventory.setItem(49, letterHead);
            inventory.setItem(50, refuseBtn());
            inventory.setItem(53, cancelBtn());

            for (int i = 0; i < items.length; i++) inventory.setItem(i + 9, items[i]);
        }
    }

    public static LetterHead getById(Player player, int id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT items_count, sent_at, sender_id, items FROM mailbox_items WHERE id = ? AND refused = false AND receiver_id = ?;")) {
            statement.setInt(1, id);
            statement.setString(2, player.getUniqueId().toString());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int itemsCount = result.getInt("items_count");
                    LocalDateTime sentAt = result.getTimestamp("sent_at").toLocalDateTime();
                    OfflinePlayer sender = Bukkit.getOfflinePlayer(UUID.fromString(result.getString("sender_id")));
                    ItemStack[] items = BukkitSerializer.deserializeItemStacks(result.getBytes("items"));
                    return new LetterHead(sender, id, itemsCount, sentAt, items);
                }
                sendFailureMessage(player, "La lettre n'a pas été trouvée.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendFailureMessage(player, "Une erreur est survenue.");
            return null;
        }
    }

    public static void refuseLetter(Player player, int id) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE mailbox_items SET refused = true WHERE id = ? AND receiver_id = ?;")) {
            statement.setInt(1, id);
            statement.setString(2, player.getUniqueId().toString());
            if (statement.executeUpdate() == 1) {
                sendSuccessMessage(player, "La lettre a été refusée.");
            } else {
                Component message = Component.text("La lettre avec l'id ", NamedTextColor.DARK_RED)
                                             .append(Component.text(id, NamedTextColor.RED))
                                             .append(Component.text(" n'existe pas.", NamedTextColor.DARK_RED));
                sendFailureMessage(player, message);
            }
        } catch (SQLException e) {
            sendFailureMessage(player, "Une erreur est survenue.");
            e.printStackTrace();
        }
    }

    private boolean getMailboxById() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT items FROM mailbox_items WHERE id = ? AND refused = false;")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    items = BukkitSerializer.deserializeItemStacks(result.getBytes("items"));
                    return true;
                }
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void accept() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM mailbox_items WHERE id = ? AND refused = false;")) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 1) {
                Component message = Component.text("Vous avez reçu ", NamedTextColor.DARK_GREEN)
                                             .append(Component.text(itemsCount, NamedTextColor.GREEN))
                                             .append(Component.text(" " + getItemCount(itemsCount), NamedTextColor.DARK_GREEN));
                sendSuccessMessage(player, message);
                HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(items);
                for (ItemStack item : remainingItems.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            } else {
                Component message = Component.text("La lettre avec l'id ", NamedTextColor.DARK_RED)
                                             .append(Component.text(id, NamedTextColor.RED))
                                             .append(Component.text(" n'existe pas.", NamedTextColor.DARK_RED));
                sendFailureMessage(player, message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendFailureMessage(player, "Une erreur est survenue.");
        }
        player.closeInventory();
    }

    public void refuse() {
        Component message = Component.text("Cliquez-ici", NamedTextColor.YELLOW)
                                     .clickEvent(getRunCommand("refuse " + id))
                                     .hoverEvent(getHoverEvent("Refuser la lettre #" + id))
                                     .append(Component.text(" si vous êtes sur de vouloir refuser la lettre.", NamedTextColor.GOLD));
        sendWarningMessage(player, message);
        player.closeInventory();
    }

    public void cancel() {
        player.closeInventory();
        sendFailureMessage(player, "La lettre a été annulée.");
    }
}
