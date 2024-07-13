package fr.communaywen.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Range;

public class TailleCommand {
    @Command({"taille", "size"})
    @Description("Change la taille du joueur")
    public void onCommand(Player player, @Named("Taille (cm)") @Range(min = 100, max = 200) int size) {
        player.sendMessage(player.getName() + " fais " + size + "cm");
        double sizeRation = ((double) size / 10) * ((double) 10 / 187);
        System.out.println(sizeRation);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "attribute " + player.getUniqueId().toString() + " minecraft:generic.scale base set " + sizeRation);
    }
}
