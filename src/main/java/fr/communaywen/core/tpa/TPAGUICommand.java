package fr.communaywen.core.tpa;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("TPAGUI")
@Credit({"Axillity"})
public class TPAGUICommand implements Listener {

    private final AywenCraftPlugin plugin;

    public TPAGUICommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpagui")
    @CommandPermission("ayw.command.tpagui")
    public void onCommand(Player player) {
        new TPACommandGUI(player, plugin).open();
    }
}
