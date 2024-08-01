package fr.communaywen.core.utils;

public class Transaction {
    public String recipient;
    public double amount;
    public String reason;
    public String sender;

    public Transaction(String recipient, String sender, double amount, String reason) {
        /*
        Recipient: Qui as reçu le paiement
            - CONSOLE pour le serveur (ex: quêtes)

        Sender: Qui as envoyé le paiement
            - CONSOLE pour le serveur (ex: adminshop)

        Amount: Montant envoyé/reçu

        Reason: Raison du paiement (transaction, achat, claim...)
         */

        this.recipient = recipient;
        this.sender = sender;
        this.amount = amount;
        this.reason = reason;
    }
}
