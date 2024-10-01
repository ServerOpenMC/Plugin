package fr.communaywen.core.commands.head;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.menu.ContributionMenu;
import fr.communaywen.core.contest.menu.VoteMenu;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.spawn.head.HeadManager;
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

@Feature("Heads")
@Credit("iambibi_")
@Command("heads")
@Description("Permet de vous donner les statistiques par rapport au tete")
public class HeadCommand {
    private final AywenCraftPlugin plugin;

    public HeadCommand(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    @Cooldown(4)
    @DefaultFor("~")
    public void defaultCommand(Player player) {

    }

    @Subcommand("give")
    @Description("Permet de lancer une procédure de phase")
    @CommandPermission("ayw.command.heads.give")
    public void give(Player player) {
        HeadManager.giveHead(player);
    }
}
