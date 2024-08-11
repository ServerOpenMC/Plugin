package fr.communaywen.core.commands.staff;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("Broadcast Dev")
@Credit("llex_tv")

public class bcDevCommands {

    @Command("bcdev")
    @CommandPermission("ayw.command.bcdev")
    @Description("Broadcast un message des développeurs")
    public void onCommand(CommandSender sender, @Named("Message") String msg){
        System.out.println("DEBUG: Commande Detect 1");
        if(sender instanceof Player){
            System.out.println("DEBUG: Instance Player");
            if(msg == null){
                Player player = (Player) sender;
                player.sendMessage("§4Veillez mettre un message.");
            }else{
                Bukkit.broadcastMessage("§bDEV - " + msg);
            }
        }
    }
}
