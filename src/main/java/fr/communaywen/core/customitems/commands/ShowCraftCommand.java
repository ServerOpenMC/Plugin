package fr.communaywen.core.customitems.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.customitems.guis.ShowCraftMainGUI;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;

@Command("showcraft")
public class ShowCraftCommand {

    @DefaultFor("~")
    public void showCraft(Player player) {

        Menu menu = new ShowCraftMainGUI(player);
        menu.open();
    }
}
