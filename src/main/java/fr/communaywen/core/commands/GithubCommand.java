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

public class GithubCommand {

    private AywenCraftPlugin plugin;

    public GithubCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command({"github", "git"})
    @Description("Affiche un lien redirigeant vers le repo github.")
    public void onCommand(Player p){
        final TextComponent textComponent = Component.text("Voici le repository du serveur : §ngithub.com/Margouta/PluginOpenMC")
                .color(TextColor.color(255,255,255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Margouta/PluginOpenMC"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour voir le repository§7]")));

        plugin.getAdventure().player(p).sendMessage(textComponent);
    }

}