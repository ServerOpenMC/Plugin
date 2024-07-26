package fr.communaywen.core.corporation.guilds.menu;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.corporation.guilds.Guild;
import fr.communaywen.core.corporation.guilds.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildSearchMenu extends PaginatedMenu {

    private final GuildManager guildManager;

    public GuildSearchMenu(Player owner, GuildManager guildManager) {
        super(owner);
        this.guildManager = guildManager;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.GRAY_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        if (guildManager.isInGuild(getOwner().getUniqueId())) {
            return StaticSlots.combine(StaticSlots.STANDARD, List.of(12, 13, 14));
        }
        return StaticSlots.STANDARD;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (Guild guild : guildManager.getGuilds()) {
            ItemStack guildItem;
            if (guildManager.isInGuild(getOwner().getUniqueId())) {
                guildItem = new ItemBuilder(this, guild.getHead(), itemMeta -> {
                    itemMeta.setDisplayName(ChatColor.YELLOW + guild.getName());
                    itemMeta.setLore(List.of(
                            ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guild.getTurnover() + "€",
                            ChatColor.GRAY + "■ Marchants : " + ChatColor.WHITE + guild.getMerchants().size(),
                            ChatColor.GRAY + "■ Cliquez pour voir les informations de la guilde"
                    ));
                }).setNextMenu(new GuildMenu(getOwner(), guild, true));
            } else {
                guildItem = new ItemBuilder(this, guild.getHead(), itemMeta -> {
                    itemMeta.setDisplayName(ChatColor.YELLOW + guild.getName());
                    itemMeta.setLore(List.of(
                            ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guild.getTurnover() + "€",
                            ChatColor.GRAY + "■ Marchants : " + ChatColor.WHITE + guild.getMerchants().size(),
                            ChatColor.GRAY + "■ Candidatures : " + ChatColor.WHITE + guildManager.getPendingApplications(guild).size(),
                            ChatColor.GRAY + "■ Cliquez pour postuler"
                    ));
                }).setOnClick((inventoryClickEvent) -> {
                    guildManager.applyToGuild(getOwner().getUniqueId(), guild);
                    getOwner().sendMessage(ChatColor.GREEN + "Vous avez postulé pour la guilde " + guild.getName() + " !");
                    guild.broadCastOwner(ChatColor.GREEN + getOwner().getName() + " a postulé pour rejoindre la guilde !");
                });
            }
            items.add(new ItemBuilder(this, guildItem));
        }
        return items;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> map = new HashMap<>();
        map.put(49, new ItemBuilder(this, Material.BARRIER, itemMeta -> itemMeta.setDisplayName(ChatColor.GRAY + "Fermer"))
                .setCloseButton());
        map.put(48, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> itemMeta.setDisplayName(ChatColor.RED + "Page précédente"))
                .setPreviousPageButton());
        map.put(50, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> itemMeta.setDisplayName(ChatColor.GREEN + "Page suivante"))
                .setNextPageButton());
        if (guildManager.isInGuild(getOwner().getUniqueId())) {
            map.put(4, new ItemBuilder(this, guildManager.getGuild(getOwner().getUniqueId()).getHead(), itemMeta -> {
                itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + guildManager.getGuild(getOwner().getUniqueId()).getName());
                itemMeta.setLore(List.of(
                        ChatColor.GRAY + "■ - Guilde -",
                        ChatColor.GRAY + "■ Chiffre d'affaires : " + ChatColor.GREEN + guildManager.getGuild(getOwner().getUniqueId()).getTurnover() + "€",
                        ChatColor.GRAY + "■ Marchants : " + ChatColor.WHITE + guildManager.getGuild(getOwner().getUniqueId()).getMerchants().size(),
                        ChatColor.GRAY + "■ Cliquez pour voir les informations de la guilde"
                ));
            }).setNextMenu(new GuildMenu(getOwner(), guildManager.getGuild(getOwner().getUniqueId()), true)));
        }
        return map;
    }

    @Override
    public @NotNull String getName() {
        return guildManager.isInGuild(getOwner().getUniqueId()) ? "Rechercher une guilde" : "Pôle travail";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
