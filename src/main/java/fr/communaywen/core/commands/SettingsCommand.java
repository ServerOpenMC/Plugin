package fr.communaywen.core.commands;

import fr.communaywen.core.settings.menu.SettingsMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;


public class SettingsCommand {
    @Command("settings")
    @Description("Configure ton profile de serveur !")
    public void openSettings(Player player) {
        new SettingsMenu(player).open();
    }
}
