package fr.communaywen.core.commands.utils;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("Wiki")
@Credit("ri1_")
public class WikiCommand {

    private final FileConfiguration wikiConfig;


    public WikiCommand(FileConfiguration wikiConfig) {
        this.wikiConfig = wikiConfig;
    }


    @Command({"wiki", "info"})
    @Description("Afficher le lien du wiki de l'item/fonctionnalité")
    @CommandPermission("ayw.command.wiki")
    @AutoComplete("@featureName")
    public void onCommand(Player player, @Named("featureName") @Optional String itemArg) {

        if (itemArg != null) {
            player.sendMessage(getOpenMCWikiChatComponent(itemArg, itemArg));
            return;
        }

        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            CustomStack customStack = CustomStack.byItemStack(player.getInventory().getItemInMainHand());
            if (customStack != null) {
                player.sendMessage(getOpenMCWikiChatComponent(customStack.getId(), customStack.getDisplayName()));
            } else {
                String link = "https://minecraft.wiki/w/" + player.getInventory().getItemInMainHand().getType().name().toLowerCase();
                Material item = player.getInventory().getItemInMainHand().getType();
                player.sendMessage(Component.text("Voici le lien du wiki de l'item ").color(NamedTextColor.YELLOW)
                        .append(Component.text(ItemUtils.getDefaultItemName(player, item))
                        .append(Component.text(" : ")
                        .append(Component.text("Cliquez ici").color(NamedTextColor.GOLD).clickEvent(ClickEvent.openUrl(link))))));
            }
        } else {
            player.sendMessage("§cVous devez tenir un item en main ou spécifier une feature pour utiliser cette commande.");
        }
    }

    public Component getOpenMCWikiChatComponent(String name, String displayName) {
        String link = wikiConfig.getString(name);
        if (link == null) {
            return Component.text("Le lien du wiki de cet item ou feature n'est pas encore disponible.").color(NamedTextColor.RED);
        }

        return Component.text("Voici le lien du wiki de l'item ou de la feature ").color(NamedTextColor.YELLOW)
                .append(Component.text(displayName)
                .append(Component.text(" : ")
                .append(Component.text("Cliquez ici").color(NamedTextColor.GOLD).clickEvent(ClickEvent.openUrl(link)))));
    }
}
