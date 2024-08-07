package fr.communaywen.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public ItemStack toItemStack(UUID player) {
        ItemStack itemstack;
        ItemMeta itemmeta;
        if (!Objects.equals(this.recipient, player.toString())) {
            itemstack = new ItemStack(Material.RED_CONCRETE, 1);
            itemmeta = itemstack.getItemMeta();
            itemmeta.setDisplayName("Transaction sortante");

            String recipient = "CONSOLE";
            if (!this.recipient.equals("CONSOLE")){
                recipient = Bukkit.getServer().getOfflinePlayer(UUID.fromString(this.recipient)).getName();
            }

            itemmeta.setLore(List.of(
                    "§r§6Recipient:§f "+recipient,
                    "§r§6Amount:§f "+this.amount,
                    "§r§6Reason:§f "+reason
            ));
        } else {
            itemstack = new ItemStack(Material.LIME_CONCRETE, 1);
            itemmeta = itemstack.getItemMeta();
            itemmeta.setDisplayName("Transaction entrante");

            String senderName = "CONSOLE";
            if (!this.sender.equals("CONSOLE")){
                senderName = Bukkit.getServer().getOfflinePlayer(UUID.fromString(this.sender)).getName();
            }

            itemmeta.setLore(List.of(
                    "§r§6Sender:§f "+senderName,
                    "§r§6Amount:§f "+this.amount,
                    "§r§6Reason:§f "+reason
            ));
        }

        itemstack.setItemMeta(itemmeta);
        return itemstack;
    }
}
