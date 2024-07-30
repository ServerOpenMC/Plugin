
package com.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.ChatColor;

public class bfb extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("heal").setExecutor(new HealCommand());
        getLogger().info("bfb v1 has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("bfb v1 has been disabled!");
    }

    public class HealCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            
if (sender instanceof Player) {
    Player player = (Player) sender;
    player.setHealth(player.getMaxHealth());
    player.setFoodLevel(20);
    player.sendMessage("You have been healed.");
} else {
    sender.sendMessage("This command can only be used by players.");
}

            return true;
        }
    }
}
