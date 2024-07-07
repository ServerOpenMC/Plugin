package fr.communaywen.core.commands;

import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class LinkCommand {
    private final LinkerAPI linkerAPI;

    public LinkCommand(LinkerAPI linkerAPI) { this.linkerAPI = linkerAPI; }

    @Command("link")
    @Description("Lie un compte Minecraft à Discord")
    public void onCommand(Player player) {
        player.sendMessage("En cours de construction");
    }
}
