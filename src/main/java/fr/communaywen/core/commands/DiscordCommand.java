package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class DiscordCommand {
    private AywenCraftPlugin plugin;

    public DiscordCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("discord")
    @Description("Affiche un lien redirigeant vers le discord.")
    public void onCommand(Player p){
        final TextComponent textComponent = Component.text("Voici le serveur discord : §ndiscord.gg/aywen-communaute-1161296442577653802")
                .color(TextColor.color(255,255,255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/invite/aywen-communaute-1161296442577653802"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour rejoindre§7]")));

        plugin.getAdventure().player(p).sendMessage(textComponent);
    }

}