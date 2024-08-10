package fr.communaywen.core.mailboxes.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class MailboxUtils {
    public static Component getPlayerName(OfflinePlayer player) {
        String pName = player.getName();
        return Component.text("⬤ ", player.isConnected() ? NamedTextColor.DARK_GREEN : NamedTextColor.DARK_RED)
                        .append(Component.text(pName == null ? "Unknown" : pName, NamedTextColor.GOLD, TextDecoration.BOLD))
                        .decoration(TextDecoration.ITALIC, false);
    }

    public static String getItemCount(int itemsCount) {
        return "item" + (itemsCount > 1 ? "s" : "");
    }

    public static ItemStack getHead(OfflinePlayer player, Component name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(nonItalic(name));
        head.setItemMeta(meta);
        return head;
    }

    public static Component colorText(String text, NamedTextColor color, boolean nonItalic) {
        Component component = Component.text(text, color);
        return nonItalic ? nonItalic(component) : component;
    }

    public static Component nonItalic(Component name) {
        return name.decoration(TextDecoration.ITALIC, false);
    }

    public static void sendWarningMessage(Player player, Component message) {
        Component success = Component.text("[", NamedTextColor.DARK_GRAY).append(Component.text("⚠", NamedTextColor.GOLD)).append(Component.text("] ", NamedTextColor.DARK_GRAY)).append(message);
        player.sendMessage(success);
    }

    public static void sendWarningMessage(Player player, String message) {
        sendWarningMessage(player, Component.text(message, NamedTextColor.GOLD));
    }

    public static void sendSuccessMessage(Player player, String message) {
        sendSuccessMessage(player, Component.text(message, NamedTextColor.DARK_GREEN));
    }

    public static void sendSuccessMessage(Player player, Component message) {
        Component success = Component.text("[", NamedTextColor.DARK_GRAY).append(Component.text("✔", NamedTextColor.DARK_GREEN)).append(Component.text("] ", NamedTextColor.DARK_GRAY)).append(message);
        player.sendMessage(success);
    }

    public static void sendFailureMessage(Player player, String message) {
        sendFailureMessage(player, Component.text(message, NamedTextColor.DARK_RED));
    }

    public static void sendFailureMessage(Player player, Component message) {
        Component success = Component.text("[", NamedTextColor.DARK_GRAY).append(Component.text("❌", NamedTextColor.DARK_RED)).append(Component.text("] ", NamedTextColor.DARK_GRAY)).append(message);
        player.sendMessage(success);
    }

    public static void sendHelp(Player sender, String message) {
        Component finalMessage = Component.text("[", NamedTextColor.DARK_GRAY).append(Component.text("help", NamedTextColor.DARK_AQUA)).append(Component.text("] ", NamedTextColor.DARK_GRAY)).append(Component.text(message, NamedTextColor.GOLD));
        sender.sendMessage(finalMessage);
    }

    public static @NotNull HoverEvent<Component> getHoverEvent(String message) {
        return HoverEvent.showText(Component.text(message, NamedTextColor.GRAY));
    }

    public static @NotNull ClickEvent getRunCommand(String command) {
        return ClickEvent.runCommand("/mailbox " + command);
    }

    public static ItemStack getHead(OfflinePlayer player) {
        return getHead(player, getPlayerName(player));
    }

    public static ItemStack getItem(Component name, Material material) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(nonItalic(name));
        item.setItemMeta(meta);
        return item;
    }
}
