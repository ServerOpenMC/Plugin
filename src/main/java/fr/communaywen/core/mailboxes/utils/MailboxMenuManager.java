package fr.communaywen.core.mailboxes.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.nonItalic;

public class MailboxMenuManager {
    public static final Key openmcKey = Key.key("openmc", "menu");
    public static final HashMap<Player, MailboxInv> playerInventories = new HashMap<>();
    private static final Material customMaterial = Material.BARRIER;

    public static Component getInvTitle(String title) {
        return Component.text(title, NamedTextColor.WHITE).font(openmcKey);
    }

    public static ItemStack getCustomItem(Component name, int data) {
        return getCustomItem(name, data, null);
    }

    public static ItemStack getCustomItem(Component name, int data, List<Component> lore) {
        ItemStack item = new ItemStack(customMaterial);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(data);
        meta.displayName(nonItalic(name));
        if (lore != null) meta.lore(lore);
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack transparentItem() {
        ItemStack item = new ItemStack(customMaterial);
        ItemMeta meta = item.getItemMeta();
        meta.setHideTooltip(true);
        meta.setCustomModelData(2005);
        meta.displayName(Component.text(""));
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getBtn(String symbol, String name, int data, NamedTextColor color) {
        return getBtn(symbol, name, data, color, false);
    }

    public static ItemStack getBtn(String symbol, String name, int data, NamedTextColor color, boolean bold) {
        Component itemName = Component.text("[", NamedTextColor.DARK_GRAY).append(Component.text(symbol, color)).append(Component.text("]", NamedTextColor.DARK_GRAY)).append(Component.text(" " + name, color));
        return getCustomItem(bold ? itemName.decorate(TextDecoration.BOLD) : itemName, data);
    }

    public static ItemStack cancelBtn() {
        return getBtn("❌", "Cancel", 2000, NamedTextColor.DARK_RED, true);
    }

    public static ItemStack nextPageBtn() {
        Component name = Component.text("Next page ➡", NamedTextColor.GOLD, TextDecoration.BOLD);
        return getCustomItem(name, 2003);
    }

    public static ItemStack previousPageBtn() {
        Component name = Component.text("⬅ Previous page", NamedTextColor.GOLD, TextDecoration.BOLD);
        return getCustomItem(name, 2004);
    }

    public static ItemStack acceptBtn() {
        return getBtn("✔", "Accepter", 2001, NamedTextColor.DARK_GREEN);
    }

    public static ItemStack sendBtn() {
        return getBtn("✉", "Envoyer", 2007, NamedTextColor.DARK_AQUA);
    }

    public static ItemStack refuseBtn() {
        return getBtn("❌", "Refuser", 2002, NamedTextColor.DARK_RED);
    }

    public static ItemStack homeBtn() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("⬅ Home", NamedTextColor.GOLD, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPlayerHead(Player player) {
        return getPlayerHead(player, Component.text(player.getName(), NamedTextColor.GOLD, TextDecoration.BOLD));
    }

    public static ItemStack getPlayerHead(Player player, Component name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(nonItalic(name));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isNotNull(ItemStack item) {
        return item != null && item.getType() != Material.AIR && item.getItemMeta() != null;
    }

    public static boolean isBtn(ItemStack item, int data) {
        return isNotNull(item) && item.getType() == customMaterial && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == data;
    }

    public static boolean cancelBtn(ItemStack item) {
        return isBtn(item, 2000);
    }

    public static boolean nextPageBtn(ItemStack item) {
        return isBtn(item, 2003);
    }

    public static boolean previousPageBtn(ItemStack item) {
        return isBtn(item, 2004);
    }

    public static boolean acceptBtn(ItemStack item) {
        return isBtn(item, 2001);
    }

    public static boolean sendBtn(ItemStack item) {
        return isBtn(item, 2007);
    }

    public static boolean refuseBtn(ItemStack item) {
        return isBtn(item, 2002);
    }
}