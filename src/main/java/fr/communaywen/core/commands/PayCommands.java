package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;

@Feature("Money")
@Credit({"Axeno", "Koneiii", "TheR0001", "llex_tv"})
public class PayCommands {
    @Command("pay")
    @Description("Transfère de l'argent d'un joueur à un autre.")
    public void payCommands(Player player, @Named("joueur") Player target, @Named("montant") int amount) {
        EconomyManager economyManager = AywenCraftPlugin.getInstance().getManagers().getEconomyManager();
        if(!player.equals(target)) {
            if(amout < 0){
                player.sendMessage("§cLa valeur doit être posiitive.");
            }else{
                if (economyManager.transferBalance(player, target, amount)) {
                    player.sendMessage("§aVous venez de transférer §e" + amount + "$ §aà §e" + target.getName());
                    target.sendMessage("§aVous venez de recevoir §e" + amount + "$ §ade la part de §e" + player.getName());
                } else {
                    player.sendMessage("§cVous n'avez pas assez d'argent.");
                }
            }
            
        } else {
            player.sendMessage("§cVous ne pouvez pas transférer de l'argent à vous-même.");
        }
    }
}
