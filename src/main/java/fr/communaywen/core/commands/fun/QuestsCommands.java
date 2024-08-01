package fr.communaywen.core.commands.fun;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.quests.QuestsMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

@Feature("Quests")
@Credit("Axeno")
public class QuestsCommands {
    @Command({"quest", "quests", "quete", "quetes"})
    @Description("Système de quêtes.")
    public void onCommand(Player player) {
        QuestsMenu menu = new QuestsMenu(player);
        menu.open();
    }
}
