package fr.communaywen.core.staff.freeze;

import fr.communaywen.core.utils.FreezeUtils;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class FreezeCommand {
    @Command("freeze")
    @Description("Freeze un joueur")
    @CommandPermission("openmc.staff.freeze")
    public void onCommand(Player player, Player target) {
        FreezeUtils.switch_freeze(player, target);
    }
}