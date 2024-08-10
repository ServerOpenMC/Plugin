package fr.communaywen.core.mailboxes.menu;

import fr.communaywen.core.mailboxes.letter.LetterHead;
import fr.communaywen.core.mailboxes.utils.PaginatedMailbox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerMailbox extends PaginatedMailbox<LetterHead> {

    static {
        invErrorMessage = "Erreur lors de la récupération de votre boite aux lettres.";
    }

    public PlayerMailbox(Player player) {
        super(player);
        if (fetchMailbox()) initInventory();
    }

    public void addLetter(LetterHead letterHead) {
        pageItems.add(letterHead);
        int size = pageItems.size();
        if (size - 1 / maxIndex == page) updateInventory(false, size - 1 % maxIndex);
    }

    public void removeLetter(int id) {
        for (int i = 0; i < pageItems.size(); i++) {
            if (pageItems.get(i).getId() == id) {
                pageItems.remove(i);
                int currentPage = i / maxIndex;

                if (currentPage == page) {
                    updateInventory(false, i % maxIndex);
                } else if (currentPage < page) {
                    updateInventory(true);
                }
                break;
            }
        }
    }

    public boolean fetchMailbox() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, sender_id, sent_at, items_count FROM mailbox_items WHERE receiver_id = ? AND refused = false ORDER BY sent_at DESC;")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    UUID senderUUID = UUID.fromString(result.getString("sender_id"));
                    int itemsCount = result.getInt("items_count");
                    LocalDateTime sentAt = result.getTimestamp("sent_at").toLocalDateTime();
                    pageItems.add(new LetterHead(Bukkit.getOfflinePlayer(senderUUID), id, itemsCount, sentAt));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
