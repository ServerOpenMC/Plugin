package fr.communaywen.core.commands;

import fr.communaywen.core.quests.QuestsMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class QuestsCommands {
    @Command({"quest", "quests", "quete", "quetes"})
    @Description("Système de quêtes.")
    public void onCommand(Player player) {
        QuestsMenu menu = new QuestsMenu(player);
        menu.open();
    }
}
