package fr.communaywen.core.commands;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ShowItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                TextComponent component = new TextComponent(itemStack.getItemMeta().getDisplayName());
                CraftItemStack.asNMSCopy(item);
                net.md_5.bungee.api.chat.hover.content.Item itemHover = new Item(itemStack.getType().getKey().toString(), itemStack.getAmount(), ItemTag.ofNbt(itemStack.getData()))

                component.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                        net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM,
                        new ComponentBuilder(itemHover.toString()).create()
                ));
                Bukkit.broadcastMessage("§l§a" + player.getDisplayName() + " vous montre : " + component);
            }
        }
        return false;
    }
}
