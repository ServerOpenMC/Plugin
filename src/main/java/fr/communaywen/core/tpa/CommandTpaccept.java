package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandTpaccept implements CommandExecutor {
    TPAQueue tpQueue = TPAQueue.INSTANCE;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player tpaplayer = tpQueue.TPA_REQUESTS.get(player);
            if (tpaplayer == null) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
                return true;
            }
            tpQueue.TPA_REQUESTS.remove(player);
            player.sendMessage(tpaplayer.getName()+" va être téléporté à vous dans 3 secondes");
            tpaplayer.sendTitle("Téléportation à "+player.getName()," dans 3 secondes...",0,20,40);
            new BukkitRunnable() {
                @Override
                public void run() {
                    tpaplayer.teleport(player);
                }
            }.runTaskLater(AywenCraftPlugin.getInstance(),60);
            return true;
        }
        return false;
    }
}
