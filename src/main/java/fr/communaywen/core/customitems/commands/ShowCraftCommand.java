package fr.communaywen.core.customitems.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.customitems.guis.ShowcraftMainGUI;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;

@Command({"showcraft", "showcrafts", "sc"})
@Credit("Fnafgameur")
public class ShowCraftCommand {

    private final CustomItemsManager customItemsManager;

    public ShowCraftCommand(CustomItemsManager customItemsManager) {
        this.customItemsManager = customItemsManager;
    }

    @DefaultFor("~")
    public void showCraft(Player player) {

        Menu menu = new ShowcraftMainGUI(player, customItemsManager);
        menu.open();
    }
}
