package fr.communaywen.core.commands.staff;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
        Bukkit.broadcastMessage("§bDEV - " + msg);
    }
}
