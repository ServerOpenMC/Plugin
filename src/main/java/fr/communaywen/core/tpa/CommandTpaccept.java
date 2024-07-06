package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandTpaccept implements CommandExecutor {

    private final AywenCraftPlugin plugin;
    TPAQueue tpQueue = TPAQueue.INSTANCE;

    public CommandTpaccept(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            TPARequest tpRequest = tpQueue.TPA_REQUESTS.get(player);
            if (tpRequest == null) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de téléporation");
                return true;
            }
            player.sendMessage(tpRequest.requeste.getName()+" va être téléporté à vous dans 3 secondes");
            tpRequest.requeste.sendTitle("Téléportation à "+player.getName()+" dans 3 secondes",null,0,20,40);
            Bukkit.getScheduler().runTaskLater(this.plugin, () ->  {
                tpRequest.requeste.teleport(player);
                tpQueue.TPA_REQUESTS.remove(player);
            },60);
            return true;
        }
        return false;
    }
}
