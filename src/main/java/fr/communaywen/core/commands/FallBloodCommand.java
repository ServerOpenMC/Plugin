package fr.communaywen.core.commands;

import fr.communaywen.core.credit.Credit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Objects;
@Credit("fuzeblocks")
public class FallBloodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.getInventory().addItem(getBandage());
                player.updateInventory();

            }
        }
        return true;

    }
    public static ItemStack getBandage() {
        ItemStack bandage = new ItemStack(Material.PAPER, 1);
        ItemMeta bandageMeta = bandage.getItemMeta();
        Objects.requireNonNull(bandageMeta).setDisplayName(ChatColor.translateAlternateColorCodes('&', "§lBandage"));
        bandage.setItemMeta(bandageMeta);
        return bandage;
    }

}
