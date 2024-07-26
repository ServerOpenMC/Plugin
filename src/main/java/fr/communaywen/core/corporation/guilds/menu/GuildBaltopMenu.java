package fr.communaywen.core.corporation.guilds.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.ItemUtils;
import fr.communaywen.core.corporation.guilds.Guild;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.guilds.data.MerchantData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuildBaltopMenu extends Menu {

    private final GuildManager guildManager;

    public GuildBaltopMenu(Player owner, GuildManager guildManager) {
        super(owner);
        this.guildManager = guildManager;
    }

    @Override
    public @NotNull String getName() {
        return "Baltop des Guildes";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        List<Guild> guilds = guildManager.getGuilds();
        guilds.sort((guild1, guild2) -> Double.compare(guild2.getTurnover(), guild1.getTurnover()));
        Map<Integer, ItemStack> content = fill(Material.GRAY_STAINED_GLASS_PANE);
        content.put(46, new ItemBuilder(this, Material.BARREL, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "Baltop des guildes");
            itemMeta.setLore(List.of(
                    ChatColor.GRAY + "■ Voici les 3 guildes les plus riches du serveur",
                    ChatColor.GRAY + "■ Les guildes sont classées en fonction de leur chiffre d'affaires"
            ));
        }));
        content.put(50, new ItemBuilder(this, Material.BARRIER, itemMeta -> itemMeta.setDisplayName(ChatColor.RED + "Fermer")).setCloseButton());
        if (guilds.isEmpty()) return content;
        content.put(10, new ItemBuilder(this, Material.GOLD_INGOT, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + "1. " + ChatColor.YELLOW + guilds.getFirst().getName());
            itemMeta.setLore(List.of(
                    ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guilds.getFirst().getTurnover() + "€",
                    ChatColor.GRAY + "■ Marchants : " + ChatColor.GREEN + guilds.getFirst().getMerchants().size()
            ));
        }));
        UUID ownerUUIDFirst;
        if (guilds.getFirst().getOwner().isTeam()) ownerUUIDFirst = guilds.getFirst().getOwner().getTeam().getOwner();
        else ownerUUIDFirst = guilds.getFirst().getOwner().getPlayer();
        content.put(12, new ItemBuilder(this, guilds.getFirst().getHead(), itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + (guilds.getFirst().getOwner().isTeam() ? guilds.getFirst().getOwner().getTeam().getName() : Bukkit.getOfflinePlayer(ownerUUIDFirst).getName()));
            itemMeta.setLore(List.of(
                    ChatColor.DARK_RED + "■ Propriétaire"
            ));
        }));
        for (int i = 13; i <= 16; i++) {
            ItemStack merchantHead;
            UUID merchantUUID;
            if (guilds.getFirst().getMerchantsUUID().size() <= i - 13) {
                merchantUUID = null;
                merchantHead = new ItemStack(Material.AIR);
            } else {
                merchantUUID = guilds.getFirst().getMerchantsUUID().get(i - 13);
                if (merchantUUID == null) merchantHead = new ItemStack(Material.AIR);
                else merchantHead = ItemUtils.getPlayerSkull(merchantUUID);
            }
            content.put(i, new ItemBuilder(this, merchantHead, itemMeta -> {
                if (merchantUUID == null) return;
                itemMeta.setDisplayName(ChatColor.DARK_GRAY + Bukkit.getOfflinePlayer(merchantUUID).getName());
                MerchantData merchantData = guilds.getFirst().getMerchants().get(merchantUUID);
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ A déposé " + ChatColor.GREEN + merchantData.getAllDepositedItemsAmount() + " items",
                        ChatColor.GRAY + "■ A gagné " + ChatColor.GREEN + merchantData.getMoneyWon() + "€"
                ));
            }));
        }
        if (guilds.size() == 1) return content;
        content.put(19, new ItemBuilder(this, Material.GOLD_INGOT, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + "2. " + ChatColor.YELLOW + guilds.get(1).getName());
            itemMeta.setLore(List.of(
                    ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guilds.get(1).getTurnover() + "€",
                    ChatColor.GRAY + "■ Marchants : " + ChatColor.GREEN + guilds.get(1).getMerchants().size()
            ));
        }));
        UUID ownerUUIDSecond;
        if (guilds.get(1).getOwner().isTeam()) ownerUUIDSecond = guilds.get(1).getOwner().getTeam().getOwner();
        else ownerUUIDSecond = guilds.get(1).getOwner().getPlayer();
        content.put(21, new ItemBuilder(this, ItemUtils.getPlayerSkull(ownerUUIDSecond), itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + (guilds.get(1).getOwner().isTeam() ? guilds.get(1).getName() : Bukkit.getOfflinePlayer(ownerUUIDSecond).getName()));
            itemMeta.setLore(List.of(
                    ChatColor.DARK_RED + "■ Propriétaire"
            ));
        }));
        for (int i = 22; i <= 25; i++) {
            ItemStack merchantHead;
            UUID merchantUUID;
            if (guilds.get(1).getMerchantsUUID().size() <= i - 22) {
                merchantUUID = null;
                merchantHead = new ItemStack(Material.AIR);
            }
            else {
                merchantUUID = guilds.get(1).getMerchantsUUID().get(i - 22);
                if (merchantUUID == null) merchantHead = new ItemStack(Material.AIR);
                else merchantHead = ItemUtils.getPlayerSkull(merchantUUID);
            }
            content.put(i, new ItemBuilder(this, merchantHead, itemMeta -> {
                if (merchantUUID == null) return;
                itemMeta.setDisplayName(ChatColor.DARK_GRAY + Bukkit.getOfflinePlayer(merchantUUID).getName());
                MerchantData merchantData = guilds.get(1).getMerchants().get(merchantUUID);
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ A déposé " + ChatColor.GREEN + merchantData.getAllDepositedItemsAmount() + " items",
                        ChatColor.GRAY + "■ A gagné " + ChatColor.GREEN + merchantData.getMoneyWon() + "€"
                ));
            }));
        }
        if (guilds.size() == 2) return content;
        content.put(28, new ItemBuilder(this, Material.GOLD_INGOT, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + "3. " + ChatColor.YELLOW + guilds.get(2).getName());
            itemMeta.setLore(List.of(
                    ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guilds.get(2).getTurnover() + "€",
                    ChatColor.GRAY + "■ Marchants : " + ChatColor.GREEN + guilds.get(2).getMerchants().size()
            ));
        }));
        UUID ownerUUIDThird;
        if (guilds.get(2).getOwner().isTeam()) ownerUUIDThird = guilds.get(2).getOwner().getTeam().getOwner();
        else ownerUUIDThird = guilds.get(2).getOwner().getPlayer();
        content.put(30, new ItemBuilder(this, ItemUtils.getPlayerSkull(ownerUUIDThird), itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + (guilds.get(2).getOwner().isTeam() ? guilds.get(2).getName() : Bukkit.getOfflinePlayer(ownerUUIDThird).getName()));
            itemMeta.setLore(List.of(
                    ChatColor.DARK_RED + "■ Propriétaire"
            ));
        }));
        for (int i = 31; i <= 34; i++) {
            ItemStack merchantHead;
            UUID merchantUUID;
            if (guilds.get(2).getMerchantsUUID().size() <= i - 31) {
                merchantUUID = null;
                merchantHead = new ItemStack(Material.AIR);
            }
            else {
                merchantUUID = guilds.get(2).getMerchantsUUID().get(i - 31);
                if (merchantUUID == null) merchantHead = new ItemStack(Material.AIR);
                else merchantHead = ItemUtils.getPlayerSkull(merchantUUID);
            }
            content.put(i, new ItemBuilder(this, merchantHead, itemMeta -> {
                if (merchantUUID == null) return;
                itemMeta.setDisplayName(ChatColor.DARK_GRAY + Bukkit.getOfflinePlayer(merchantUUID).getName());
                MerchantData merchantData = guilds.get(2).getMerchants().get(merchantUUID);
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ A déposé " + ChatColor.GREEN + merchantData.getAllDepositedItemsAmount() + " items",
                        ChatColor.GRAY + "■ A gagné " + ChatColor.GREEN + merchantData.getMoneyWon() + "€"
                ));
            }));
        }
        return content;
    }
}
