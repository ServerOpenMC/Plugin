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

public class PongCommand {

    private AywenCraftPlugin plugin;

    public PongCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Command({"ping"})
    @Description("Répondre Pong !")
    public void onCommand(Player p) {
        final TextComponent textComponent = Component.text("§a§lPONG !")
                .color(TextColor.color(255, 255, 255))

        plugin.getAdventure().player(p).sendMessage(textComponent);
    }

}