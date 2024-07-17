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

public class RickCommand {

    private AywenCraftPlugin plugin;

    public RickCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Command({"rick"})
    @Description("Never Gonna Give You UP !")
    public void onCommand(Player p) {
        final TextComponent textComponent = Component.text("§d§l???")
                .color(TextColor.color(255, 255, 255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://youtu.be/dQw4w9WgXcQ?si=ZQDA1pFymhPsjhlC"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour voir...§7]")));

        plugin.getAdventure().player(p).sendMessage(textComponent);
    }

}