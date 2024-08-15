package fr.communaywen.core.commands.randomEvents;

import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;


@Credit("Armibule")
@Feature("EventsCommand")
public class RandomEventsCommand {

    private AywenCraftPlugin plugin;

    public RandomEventsCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("events")
    @Description("Ouvre le menu des évènements aléatoires")
    public void adminShop(Player player) {
        RandomEventsMenu menu = new RandomEventsMenu(player, plugin);
        menu.open();
    }

}
