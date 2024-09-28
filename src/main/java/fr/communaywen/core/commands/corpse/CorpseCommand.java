package fr.communaywen.core.commands.corpse;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.menu.ContributionMenu;
import fr.communaywen.core.contest.menu.VoteMenu;
import fr.communaywen.core.corpse.CorpseBlock;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Credit("iambibi_")
@Command("corpse")
@Description("Corpse")
public class CorpseCommand {
    private final AywenCraftPlugin plugin;
    public CorpseCommand(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    @Subcommand("remove")
    @Description("Supprimer une tombe")
    @CommandPermission("ayw.command.corpse.remove")
    public void remove(Player player) {
        CorpseBlock corpse = CorpseManager.getCorpseBlock(player.getTargetBlock(null, 100).getLocation());

        if (corpse != null) {
            CorpseManager.remove(corpse);
            player.sendMessage("§aTombe supprimé avec succès");
        } else {
            player.sendMessage("§cIl n'y a pas de tombe qui est target (Veuillez pointez la tombe)");
        }
    }

}
