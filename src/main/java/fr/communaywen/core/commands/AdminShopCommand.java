package fr.communaywen.core.commands;

import fr.communaywen.core.adminshop.menu.AdminShopMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class AdminShopCommand {

    @Command("adminshop")
    @Description("Ouvre l'adminshop")
    public void adminShop(Player player) {
        AdminShopMenu menu = new AdminShopMenu(player);
        menu.open();
    }

}
