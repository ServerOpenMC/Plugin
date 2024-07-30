// Je n'ai pas de inspi :333333333
// Made by Axillity
// ne bannez pas moi uwu
package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CacaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être exécutée que par un joueur.");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        // Déposer un "caca" à la position du joueur
        ItemStack cacaItem = new ItemStack(Material.BROWN_WOOL); // Vous pouvez changer le matériau si désiré
        location.getWorld().dropItemNaturally(location, cacaItem);

        // Envoyer un message à tous les joueurs
        String message = ChatColor.YELLOW + player.getName() + " a fait caca à sa position!";
        Bukkit.broadcastMessage(message);

        return true;
    }
}
