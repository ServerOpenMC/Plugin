package fr.communaywen.core.commands;

import fr.communaywen.core.credit.Credit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Objects;

@Credit("fuzeblocks")
public class FallBloodCommand {

    @Command("fallblood")
    @Description("Give a bandage")
    @CommandPermission("ayw.command.use.fallblood")
    public void giveCommands(Player player) {
        player.getInventory().addItem(getBandage());
        player.updateInventory();
    }

    public static ItemStack getBandage() {
        ItemStack bandage = new ItemStack(Material.PAPER, 1);
        ItemMeta bandageMeta = bandage.getItemMeta();
        Objects.requireNonNull(bandageMeta).setDisplayName(ChatColor.translateAlternateColorCodes('&', "§lBandage"));
        bandage.setItemMeta(bandageMeta);
        return bandage;
    }

}
