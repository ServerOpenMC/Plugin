package fr.communaywen.core.commands.fun;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Range;

@Feature("Taille")
@Credit("Gyro3630")
public class TailleCommand {
    @Command({"taille", "size"})
    @Description("Change la taille du joueur")
    public void onCommand(Player player, @Named("Taille (cm)") @Range(min = 100, max = 200) int size) {
        player.sendMessage(player.getName() + " fais " + size + "cm");
        double sizeRation = ((double) size / 10) * ((double) 10 / 187);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "attribute " + player.getUniqueId().toString() + " minecraft:generic.scale base set " + sizeRation);
    }
}
