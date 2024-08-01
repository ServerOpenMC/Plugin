package fr.communaywen.core.utils.database;

import fr.communaywen.core.utils.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionsManager extends DatabaseConnector {
    public List<Transaction> getTransactionsByPlayers(UUID player) {
        List<Transaction> transactions = new ArrayList<Transaction>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE recipient = ? AND sender = ?");
            statement.setString(1, player.toString());
            statement.setString(2, player.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getString("recipient"),
                        rs.getString("sender"),
                        rs.getDouble("amount"),
                        rs.getString("reason")
                ));
            }

            return transactions;
        } catch (SQLException err) {
            err.printStackTrace();
            return List.of(new Transaction("CONSOLE", "CONSOLE", 0, "ERREUR"));
        }
    }

    public boolean addTransaction(Transaction transaction) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO transactions VALUES (?, ?, ?, ?)");
            statement.setString(1, transaction.recipient);
            statement.setString(2, transaction.sender);
            statement.setDouble(3, transaction.amount);
            statement.setString(4, transaction.reason);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
