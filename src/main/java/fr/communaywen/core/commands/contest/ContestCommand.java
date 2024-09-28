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
import revxrsal.commands.annotation.*;
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
    public ContestCommand(AywenCraftPlugin plugins, FileConfiguration eventConfigs) {
        plugin = plugins;
        eventConfig = eventConfigs;
    }

    @Cooldown(4)
    @DefaultFor("~")
    public void defaultCommand(Player player) {
        int phase = ContestManager.getPhaseCache();
        int camp = ContestManager.getPlayerCampsCache(player);
        if (phase==2) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else if (phase==3 && camp == 0) {
            VoteMenu menu = new VoteMenu(player);
            menu.open();
        } else if (phase==3) {
            ContributionMenu menu = new ContributionMenu(player, plugin);
            menu.open();

        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
            DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestManager.getStartDateCache()));

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

    @Subcommand("setcontest")
    @Description("Permet de définir un Contest")
    @CommandPermission("ayw.command.contest.setcontest")
    @AutoComplete("@colorContest")
    public void setcontest(Player player, String camp1, @Named("colorContest") String color1, String camp2, @Named("colorContest") String color2) {
        int phase = ContestManager.getPhaseCache();
        if (phase == 1) {
            if (ContestManager.getColorContestList().contains(color1) || ContestManager.getColorContestList().contains(color2)) {
                ContestManager.deleteTableContest("contest");
                ContestManager.deleteTableContest("camps");
                ContestManager.insertCustomContest(camp1, color1, camp2, color2);

                player.sendMessage("§aLe Contest : " + camp1 + " VS " + camp2 + " a bien été sauvegarder\nMerci d'attendre que les données en cache s'actualise.");
            } else {
                player.sendMessage("§c/contest setcontest <camp1> <color1> <camp2> <color2> et color doit comporter une couleur valide");
            }
        } else {
            player.sendMessage("§cVous pouvez pas définir un contest lorsqu'il a commencé");
        }
    }

    @Subcommand("addpoints")
    @Description("Permet d'ajouter des points a un membre")
    @CommandPermission("ayw.command.contest.addpoints")
    public void addpoints(Player player, Player target, Integer points) {
        ContestManager.addPointPlayer(points + ContestManager.getPlayerPoints(target), target);

        player.sendMessage("§aVous avez ajouté " + points + " §apoint(s) à " + target.getName());
    }

}
