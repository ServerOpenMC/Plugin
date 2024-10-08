package fr.communaywen.core.commands.staff;

import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.utils.FreezeUtils;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Credit("Process")
@Collaborators("gab400")

public class FreezeCommand {
    @Command("freeze")
    @Description("Freeze un joueur")
    @CommandPermission("openmc.staff.freeze")
    @AutoComplete("freeze *")
    public void onCommand(Player player, Player target) {
        FreezeUtils.switch_freeze(player, target);
    }
}