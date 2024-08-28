package fr.communaywen.core.commands.contest;

import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.menu.ContributionMenu;
import fr.communaywen.core.contest.menu.VoteMenu;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Feature("Contest")
@Credit("iambibi_")
public class ContestCommand {

    @Command("contest")
    @Description("Ouvre l'interface des festivals, et quand un festival commence, vous pouvez choisir votre camp")
    public void onCommand(Player player) {
        int phase = ContestManager.getInt("contest","phase");
        String dayStartContest = ContestManager.getString("startdate");
        int camp = ContestManager.getPlayerCamp(player);
        if (phase==2) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else if (phase==3) {
            ContributionMenu menu = new ContributionMenu(player);
            menu.open();

        } else if (phase==3 && camp == 0) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
            DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(dayStartContest));

                int days = (dayStartContestOfWeek.getValue() - ContestManager.getCurrentDayOfWeek().getValue() + 7) % 7;

                player.sendMessage(ChatColor.RED + "Il n'y a aucun Contest ! Revenez dans " + days + " jours.");
        }

    }

}
