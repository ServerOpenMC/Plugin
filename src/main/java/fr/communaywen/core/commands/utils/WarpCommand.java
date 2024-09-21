package fr.communaywen.core.commands.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import fr.communaywen.core.warp.WarpMenu;


@Feature("Warp")
@Credit("iambibi_")
@Command("warp")
@Description("Ouvre le globe, pour pouvoir aller à des endroits emblématiques")
public class WarpCommand {
    private final AywenCraftPlugin plugin;
    public WarpCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("~")
    public void defaultCommand(Player player) {
        WarpMenu menu = new WarpMenu(player);
        menu.open();
    }
}