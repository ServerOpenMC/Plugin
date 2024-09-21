package fr.communaywen.core.commands.contest;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.menu.ContributionMenu;
import fr.communaywen.core.contest.menu.VoteMenu;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Feature("Contest")
@Credit("iambibi_")
@Command("contest")
@Description("Ouvre l'interface des festivals, et quand un festival commence, vous pouvez choisir votre camp")
public class ContestCommand {
    private final AywenCraftPlugin plugin;
    private final FileConfiguration eventConfig;
    public ContestCommand(AywenCraftPlugin plugin, FileConfiguration eventConfig) {
        this.plugin = plugin;
        this.eventConfig = eventConfig;
    }

    @DefaultFor("~")
    public void defaultCommand(Player player) {
        int phase = ContestManager.getInt("contest","phase");
        String dayStartContest = ContestManager.getString("contest","startdate");
        int camp = ContestManager.getPlayerCamp(player);
        if (phase==2) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else if (phase==3 && camp == 0) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else if (phase==3) {
            ContributionMenu menu = new ContributionMenu(player, this.plugin);
            menu.open();

        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
            DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(dayStartContest));

            int days = (dayStartContestOfWeek.getValue() - ContestManager.getCurrentDayOfWeek().getValue() + 7) % 7;

            MessageManager.sendMessageType(player, "§cIl n'y a aucun Contest ! Revenez dans " + days + " jours.", Prefix.CONTEST, MessageType.ERROR, true);
        }

    }

    @Subcommand("setphase")
    @Description("Permet de lancer une procédure de phase")
    @CommandPermission("ayw.command.contest.setphase")
    public void setphase(Integer phase) {
        if (phase == 1) {
            ContestManager.initPhase1();
        } else if (phase == 2) {
            ContestManager.initPhase2(plugin, eventConfig);
        } else if (phase == 3) {
            ContestManager.initPhase3(plugin, eventConfig);
        }
    }

}
