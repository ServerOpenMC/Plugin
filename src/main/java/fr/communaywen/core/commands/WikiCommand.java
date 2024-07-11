package fr.communaywen.core.commands;

import dev.lone.itemsadder.api.CustomStack;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;


public class WikiCommand {

    private final FileConfiguration wikiConfig;


    public WikiCommand(FileConfiguration wikiConfig) {
        this.wikiConfig = wikiConfig;
    }




    @Command({"wiki", "info"})
    @Description("Afficher le lien du wiki de l'item/fonctionnalité")
    @CommandPermission("ayw.command.wiki")
    @AutoComplete("@featureName")
    public void onCommand(Player player, @Named("featureName") @Optional  String itemArg) {

        if(itemArg != null) {
            player.spigot().sendMessage(getOpenMCWikiChatComponent(itemArg, itemArg));
            return;
        }


        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            CustomStack customStack = CustomStack.byItemStack(player.getInventory().getItemInMainHand());
            if (customStack != null) {
                player.spigot().sendMessage(getOpenMCWikiChatComponent(customStack.getId(), customStack.getDisplayName()));
            } else {
                String link = "https://minecraft.wiki/w/"+player.getInventory().getItemInMainHand().getType().name().toLowerCase();
                TranslatableComponent itemName = new TranslatableComponent(player.getInventory().getItemInMainHand().getType().getTranslationKey());
                BaseComponent[] component =
                        new ComponentBuilder("§eVoici le lien du wiki de l'item ").append(itemName).color(ChatColor.GOLD).append(" §e: §6Minecraft Wiki")
                                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
                                .create();
                player.spigot().sendMessage(component);
            }
        } else {
            player.sendMessage("§cVous devez tenir un item en main ou spécifier une feature pour utiliser cette commande.");
        }


    }

    public BaseComponent[] getOpenMCWikiChatComponent(String name, String displayName) {
        String link = wikiConfig.getString(name);
        if(link == null) {
            return new ComponentBuilder("§cLe lien du wiki de cet item ou feature n'est pas encore disponible.").create();
        }

        BaseComponent[] component =
                new ComponentBuilder("§eVoici le lien du wiki de l'item ou de la feature ").append(displayName).color(ChatColor.GOLD).append(" §e: §6OpenMC Wiki")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
                        .create();
        return component;
    }
}
